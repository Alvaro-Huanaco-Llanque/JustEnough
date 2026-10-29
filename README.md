# JustEnough
El proyecto utiliza software y ciencia de datos para generar predicciones de demanda, permitiendo anticiparse a los requerimientos reales de los clientes. De esta manera, se busca minimizar el desperdicio de alimentos y asegurar una producción ajustada a la demanda esperada

Cómo encender los 3 repositorios
Requisitos previos (una sola vez)
MySQL Server 8.x: base de datos del backend.
JDK 25: para el backend (Spring Boot).
Node.js 18 o superior: para el frontend (React + Vite).
Python 3.11 o superior: para el servicio de IA.
VS Code con el Extension Pack for Java: para correr el backend fácil.

Si no tenés el JDK 25 instalado, VS Code usa el que trae la extensión de Java. Por eso conviene encender el backend desde Run and Debug y no desde la terminal.
Orden para encender: MySQL → Backend → Frontend. La IA es independiente.


Encender backend
Puerto: http://localhost:8080

1. Tener MySQL corriendo. La base de datos se crea sola la primera vez; no hace falta crearla a mano.

2. Crear el archivo backend/.env (no se sube a GitHub, cada uno tiene el suyo):
DB_NAME=justenough
DB_USER=root
DB_PASSWORD=tu_contraseña_de_mysql
MAIL_USERNAME=cuenta_del_proyecto@gmail.com
MAIL_PASSWORD=contraseña_de_aplicacion_de_16_letras
DB_USER y DB_PASSWORD son los de tu MySQL.
MAIL_USERNAME y MAIL_PASSWORD son los del Gmail del proyecto (pedíselos al equipo por privado). MAIL_PASSWORD es una contraseña de aplicación de Google, sin espacios, no la contraseña normal del Gmail.
Si dejás afuera las líneas de mail, la app funciona igual: el link para verificar cuentas nuevas aparece en la consola del backend en vez de llegar por mail.

3. Encenderlo, de una de estas dos formas:
Opción A (recomendada), desde VS Code: abrir la carpeta del proyecto → Run and Debug (Ctrl + Shift + D) → elegir Spring Boot-Application<core-backend> → F5.
Opción B, desde la terminal (requiere JDK 25 instalado), parado en la carpeta backend:
./mvnw spring-boot:run

4. Comprobar que arrancó: en la consola tiene que aparecer Started Application.
Cuenta de prueba del equipo (se crea sola al arrancar):
Email: jetest@gmail.com
Contraseña: jetest123
Después de cada git pull, reiniciar el backend (Ctrl + Shift + F5) para que tome los cambios. Si VS Code pregunta “synchronize the Java classpath?”, tocar Yes.
Encender frontend
Puerto: http://localhost:5173



Parado en la carpeta frontend:

1. Instalar las dependencias (solo la primera vez, o cuando cambie package.json):
npm install

2. Encenderlo:
npm run dev

3. Abrir en el navegador: http://localhost:5173
El frontend necesita el backend encendido para iniciar sesión y cargar datos.
Encender IA
Puerto: http://localhost:8000
Parado en la carpeta ia:

1. Crear el entorno virtual (solo la primera vez):
python -m venv .venv

2. Activarlo (cada vez que abras una terminal nueva):
Windows (PowerShell):
.venv\Scripts\Activate.ps1
Mac / Linux:
source .venv/bin/activate

3. Instalar las dependencias (solo la primera vez, o cuando cambie requirements.txt):
pip install -r requirements.txt

4. Encenderlo:
uvicorn main:app --reload --port 8000

5. Comprobar que anda: abrir http://localhost:8000. Tiene que responder “Servicio predictivo de JustEnough funcionando correctamente”. La documentación de la API está en http://localhost:8000/docs.