# ⚡ WLED Luces App (Android)

Aplicación móvil nativa para Android que sincroniza luces **WLED** en tiempo real utilizando el micrófono de tu teléfono celular.

## ✨ Características
- 🎤 **Micrófono del celular en vivo**: Captura con baja latencia y análisis por Transformada de Fourier (FFT).
- ⚡ **Envío UDP en tiempo real**: Envía tramas binarias DRGB al puerto `21324` de tu WLED (ESP8266 / ESP32).
- 🔒 **Funciona con la pantalla apagada**: Servicio en segundo plano con WakeLock para no detener la fiesta.
- 🎨 **Efectos incluidos**:
  - **Espectro Global**: 1 sola onda central masiva.
  - **Espectro Vocal**: Ondas suaves en cada pared.
  - **Contorno Vocal**: Anillo perimetral que reacciona a la voz.
  - **Color Sólido**: Luz fija y limpia sin parpadeos.
  - **Rotor, Pulso, Ondas, Paredes, Scanner, Espectro, etc.**
- 🌈 **Paletas de color**:
  - Paletas rojas y moradas (`rojo_morado`, `morado_rojo`, `neon_vampire`, `dark_sith`).
  - Modo **🎲 Aleatoria** con temporizador ajustable (1s, 5s, 8s, etc.).

## 📲 Cómo instalar en tu teléfono
1. Ve a la pestaña **[Releases](../../releases)** o **[Actions](../../actions)** de este repositorio.
2. Descarga el archivo **`app-debug.apk`** en tu teléfono Android.
3. Tócalo para instalar (permite "Instalar aplicaciones desconocidas" en tu navegador si te lo pide).
4. Abre la app, concede permiso de micrófono y ¡listo!
