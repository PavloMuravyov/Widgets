package widgets.widgetsModule.widgets.celsius

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder
import widgets.widgetsModule.data.models.WidgetSizes
import widgets.widgetsModule.widgets.celsius.ui.CelsiusLargeView.CelsiusLargeView
import widgets.widgetsModule.widgets.celsius.ui.CelsiusMediumView.CelsiusMediumView
import widgets.widgetsModule.widgets.celsius.ui.CelsiusSmallView.CelsiusSmallView
import widgets.widgetsModule.widgetsManager.Utils.StateHolders.CapturedLayerState

@Composable
fun CelsiusView(celsiusViewModel: CelsiusViewModel, capturedLayerState: CapturedLayerState) {

    val widgetSize by celsiusViewModel.widgetSize.collectAsState()





    when (widgetSize) {

        WidgetSizes.Small -> {
            CelsiusSmallView(
                celsiusViewModel,
                capturedLayerState
            )
        }

        WidgetSizes.Medium -> {
            CelsiusMediumView(
                celsiusViewModel,
                capturedLayerState
            )
        }

        WidgetSizes.Large -> {
            CelsiusLargeView(
                celsiusViewModel,
                capturedLayerState
            )
        }
    }

}



object SkyPresets {

    // Закат: золоте світло, фіолетово-рожеві тіні
    val Sunset = SkyState(
        coverage = 0.55f,
        softness = 0.25f,
        wind = Offset(0.015f, 0.003f),
        light = Color(0xFFFFC27A),
        shadow = Color(0xFF8A4F7D),
    )

    // Гроза: щільні темні хмари, швидший вітер
    val Storm = SkyState(
        coverage = 0.85f,
        softness = 0.12f,
        wind = Offset(0.05f, 0.01f),
        light = Color(0xFF8E98A3),
        shadow = Color(0xFF1F252C),
    )

    // Ніч: рідкі хмари з холодним місячним підсвічуванням
    val Night = SkyState(
        coverage = 0.4f,
        softness = 0.3f,
        wind = Offset(0.008f, 0.0f),
        light = Color(0xFF5C6F9C),
        shadow = Color(0xFF0B1226),
    )

    // Ясно, кілька хмар
    val Clear = SkyState(
        coverage = 0.32f,
        softness = 0.3f,
        wind = Offset(0.01f, 0.002f),
        light = Color(0xFFFFFFFF),
        shadow = Color(0xFFB8CBE6),
    )
}
data class SkyState(
    val coverage: Float,
    val softness: Float,
    val wind: Offset,
    val light: Color,
    val shadow: Color,
)

@Composable
fun CloudLayer(sky: SkyState, modifier: Modifier = Modifier) {
    val builder = remember {
        RuntimeShaderBuilder(RuntimeEffect.makeForShader(CLOUD_SKSL))
    }
    // читається лише в draw-фазі → без рекомпозиції
    val time = remember { mutableFloatStateOf(0f) }
    LaunchedEffect(Unit) {
        while (true) withFrameNanos { time.floatValue = it / 1_000_000_000f }
    }

    // плавні переходи між станами погоди
    val coverage by animateFloatAsState(sky.coverage, tween(2000))
    val softness by animateFloatAsState(sky.softness, tween(2000))

    Box(modifier.drawWithCache {
        val paint = Paint()
        onDrawBehind {
            builder.uniform("resolution", size.width, size.height)
            builder.uniform("time", time.floatValue)
            builder.uniform("coverage", coverage)
            builder.uniform("softness", softness)
            builder.uniform("wind", sky.wind.x, sky.wind.y)
            builder.uniform("sunDir", 0.6f, -0.8f)
            builder.uniform("lightColor", sky.light.red, sky.light.green, sky.light.blue)
            builder.uniform("shadowColor", sky.shadow.red, sky.shadow.green, sky.shadow.blue)

            paint.shader = builder.makeShader()
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawRect(
                    Rect.makeWH(size.width, size.height),
                    paint
                )
            }
        }
    })

}

private const val CLOUD_SKSL = """
uniform float2 resolution;
uniform float  time;
uniform float  coverage;
uniform float  softness;
uniform float2 wind;
uniform float2 sunDir;
uniform float3 lightColor;
uniform float3 shadowColor;

float hash(float2 p) {
    float3 p3 = fract(float3(p.xyx) * 0.1031);
    p3 += dot(p3, p3.yzx + 33.33);
    return fract((p3.x + p3.y) * p3.z);
}

float noise(float2 p) {
    float2 i = floor(p);
    float2 f = fract(p);
    f = f * f * f * (f * (f * 6.0 - 15.0) + 10.0); // quintic, без «сітки»
    return mix(mix(hash(i), hash(i + float2(1, 0)), f.x),
               mix(hash(i + float2(0, 1)), hash(i + float2(1, 1)), f.x), f.y);
}

// повна версія: форма хмари
float fbm5(float2 p) {
    float v = 0.0, a = 0.5;
    float2x2 m = float2x2(0.8, 0.6, -0.6, 0.8) * 2.02;
    for (int i = 0; i < 5; i++) {
        v += a * noise(p);
        p = m * p;
        a *= 0.5;
    }
    return v;
}

// спрощена: для кроків до сонця
float fbm3(float2 p) {
    float v = 0.0, a = 0.5;
    float2x2 m = float2x2(0.8, 0.6, -0.6, 0.8) * 2.02;
    for (int i = 0; i < 3; i++) {
        v += a * noise(p);
        p = m * p;
        a *= 0.5;
    }
    return v;
}

float edge0() { return 1.0 - coverage; }

float densityFull(float2 p) {
    float n = fbm5(p);
    return smoothstep(edge0(), edge0() + softness + 0.15, n);
}

float densityLow(float2 p) {
    float n = fbm3(p);
    return smoothstep(edge0(), edge0() + softness + 0.15, n);
}

half4 main(float2 fragCoord) {
    // розтягуємо по горизонталі
    float2 uv = (fragCoord / resolution.y) * float2(2.0, 3.2);
    float2 drift = wind * time;

    float d = densityFull(uv + drift);
    if (d < 0.002) return half4(0.0);

    // світло: накопичуємо густину в бік сонця
    float2 stepV = normalize(sunDir) * 0.07;
    float acc = 0.0;
    for (int i = 1; i <= 4; i++) {
        acc += densityLow(uv + drift + stepV * float(i));
    }
    float light = exp(-acc * 0.9);

    // тонкі краї світлі (світло просвічує), густе ядро темніше
    float thin = 1.0 - d;
    light = clamp(light + thin * thin * 0.35, 0.0, 1.0);

    float3 col = mix(shadowColor, lightColor, light);

    // м'яка альфа: центр щільний, краї напівпрозорі
    float alpha = pow(d, 0.8);
    return half4(half3(col * alpha), half(alpha));
}
"""
