# 🏪 Proyecto: Sistema de Administración de Tienda

## 📋 Descripción

Este proyecto corresponde al curso **Programación II (ITI-321)** de la carrera de Ingeniería en Tecnologías de Información de la **Universidad Técnica Nacional (UTN)**.

El sistema implementa una **aplicación de escritorio en Java** orientada a objetos para administrar una tienda que gestiona:
- 🗂️ **Categorías de productos**
- 📦 **Productos vinculados a sus categorías**
- 👤 **Clientes y sus métodos de pago**
- 🧾 **Facturas y sus ítems**
- 📢 **Notificaciones por diferentes canales** (correo, SMS, WhatsApp o pantalla)

El diseño sigue principios de **buenas prácticas**, **encapsulamiento**, **separación por capas** y aplicación progresiva de **patrones de diseño**.

---

## 🧠 Objetivos de aprendizaje

1. Aplicar conceptos de **Programación Orientada a Objetos (POO)** en Java.
2. Implementar **patrones de diseño y arquitectura** para mejorar mantenibilidad y extensibilidad.
3. Desarrollar software modular y escalable con separación entre lógica de negocio y persistencia.
4. Comprender la importancia del diseño limpio y la codificación segura.

---

## ⚙️ Estructura del proyecto
📦 src/
├── App.java # Clase principal
│
├── 🗂️ Catálogo
│ ├── Categoria.java
│ ├── Producto.java
│ ├── RepositorioCategorias.java
│ ├── RepositorioProductos.java
│ └── ServicioCatalogo.java
│
├── 👤 Clientes
│ ├── Cliente.java
│ ├── MetodoPago.java
│ ├── TipoMetodoPago.java
│ ├── RepositorioClientes.java
│ └── ServicioClientes.java
│
├── 🧾 Facturación
│ ├── Factura.java
│ ├── ItemFactura.java
│ ├── EstadoFactura.java
│ ├── RepositorioFacturas.java
│ └── ServicioFacturacion.java
│
├── 📢 Notificaciones
│ ├── CanalNotificacion.java
│ ├── EstadoNotificacion.java
│ ├── Notificacion.java
│ └── ServicioNotificaciones.java
│
└── README.md

## 🧩 Patrones de diseño aplicados
Patrón de diseño aplicado: Singleton -> Marisol
Dónde se aplicó: Clase RepositorioFacturas
Justificación: Solo tiene que haber un solo historial de 
facturas en el proyecto, hace que no hayan dos facturas 
iguales, errores o que use memoria de más, solo quedaría una instancia
