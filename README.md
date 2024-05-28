<h1 align="center">Time Wise</h1>

<p align="center">  
  Aplicación que permite organizar tareas pendientes a realizar. Clasificación por etiquetas, fechas de vencimiento y recordatorios con notificaciones push. Control de tareas pendientes por fecha de vencimiento: Tareas para hoy, esta semana, más tarde o expiradas. Con el buscador se permite localizar cualquier tarea, finalizada o no.
</p>
<p align="center">   
  Aplicicación Android basada en la arquitectura MVVM desarrollada con DaggerHilt, StateFlows, ViewModels, Room, Notificaciones Push y Testing
</p>

## 🛠 Herramientas y librerias
- Basado en lenguaje [Kotlin](https://kotlinlang.org/) con una interfaz en XML / Jetpack Compose
- Arquitectura MVVM (Model-View-ViewModel)
- ViewModel y StateFlow: Nos permite almacenar el estado y realizar cambios de forma reactiva en la interfaz de usuario.
- Lifecycle: Observador de los ciclos de vida de Androrid. Los usamos para recolectar los cambios de estado en el StateFlow para modificar la interfaz del usuario.
- Room: Base de datos local sobre SQLite para permitirnos un acceso fluido, eficiente y seguro.
- [Dagger Hilt](https://dagger.dev/hilt/) para inyección de dependencias.
- Testing
- Código con Clean Code y Clean Architecture

## 📱 Capturas
| Main | Tasks | Detail task |
|--|--|--|
| <img src="/previews/MainActivity.webp" width="245" height="500"> | <img src="/previews/TaskActivity.webp" width="245" height="500"> | <img src="/previews/DetailTaskActivity.webp" width="245" height="500">

| Add label | Search tasks | Filtered tasks |
|--|--|--|
| <img src="/previews/AddLabel.webp" width="245" height="500"> | <img src="/previews/SearchActivity.webp" width="245" height="500"> | <img src="/previews/FilteredActivity.webp" width="245" height="500">

## 👇 Descargar 👇
Ir a [Releases](https://github.com/AudyDevs/TimeWise/releases) para descargar el último APK.
