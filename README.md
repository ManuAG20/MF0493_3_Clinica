# Clínica Salud · MF0493_3

Aplicación web de gestión de **pacientes** y **citas** construida con **Java 17, Spring Boot, Spring MVC, Hibernate, Thymeleaf y Maven**.

## Funcionalidad

- CRUD completo de pacientes.
- CRUD completo de citas.
- Relación 1:N entre `Paciente` y `Cita` (`@OneToMany` / `@ManyToOne`).
- Validación de formularios con mensajes por campo.
- Gestión centralizada de errores (404 y 500).
- 22 pruebas unitarias y de integración con JUnit 5, Mockito y MockMvc.

## Ejecución

```bat
arrancar.bat
```

Abrir <http://localhost:8080>. Para parar, `Ctrl + C`.

## Pruebas

```bat
probar.bat
```

## Empaquetado y despliegue

```bat
empaquetar.bat
java -jar target\app.jar
```

En otro equipo solo se necesita **Java 17** y el fichero `app.jar`.

## Estructura

- `model`: entidades JPA `Paciente` y `Cita`.
- `repository`: repositorios Spring Data JPA.
- `service`: lógica de negocio y validación.
- `controller`: controladores Spring MVC.
- `exception`: excepciones y gestión global de errores.
- `templates`: vistas Thymeleaf.
- `schema.sql` y `data.sql`: creación de tablas y datos de prueba.

## Documentación

La documentación completa está en `documentacion/DOCUMENTACION.html` y en el PDF generado.
