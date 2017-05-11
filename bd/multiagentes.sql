-- CREATE DATABASE IF NOT EXISTS multiagentes;
-- USE multiagentes;

DROP TABLE IF EXISTS estudiante_evaluacion;
DROP TABLE IF EXISTS respuestas_estudiante_evaluacion;
DROP TABLE IF EXISTS respuestas_pregunta;
DROP TABLE IF EXISTS respuesta;
DROP TABLE IF EXISTS preguntas_evaluacion;
DROP TABLE IF EXISTS pregunta;
DROP TABLE IF EXISTS evaluacion;
DROP TABLE IF EXISTS asignatura;
DROP TABLE IF EXISTS estudiantes;


CREATE TABLE estudiantes (
	id INT AUTO_INCREMENT,
	nombre varchar(50) NOT NULL,
	apellido varchar(50) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE asignatura (
    id INT AUTO_INCREMENT,
  	descripcion varchar(200) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE evaluacion (
	id INT AUTO_INCREMENT,
	id_asignatura INT,
	descripcion varchar(200) NOT NULL,
	simulacro boolean DEFAULT false,
	PRIMARY KEY (id),
	FOREIGN KEY (id_asignatura) REFERENCES asignatura(id) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE pregunta (
	id INT AUTO_INCREMENT,
	descripcion varchar(200) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE preguntas_evaluacion (
	id_pregunta int NOT NULL,
	id_evaluacion int NOT NULL,
	PRIMARY KEY (id_pregunta, id_evaluacion),
	FOREIGN KEY (id_pregunta) REFERENCES pregunta(id) ON DELETE CASCADE,
	FOREIGN KEY (id_evaluacion) REFERENCES evaluacion(id) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE respuesta (
	id INT AUTO_INCREMENT,
	descripcion varchar(200) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=INNODB;

CREATE TABLE respuestas_pregunta (
	id_pregunta int NOT NULL,
	id_respuesta int NOT NULL,
	correcta boolean DEFAULT false,
	PRIMARY KEY (id_pregunta, id_respuesta),
	FOREIGN KEY (id_respuesta) REFERENCES respuesta(id) ON DELETE CASCADE,
	FOREIGN KEY (id_pregunta) REFERENCES pregunta(id) ON DELETE CASCADE
) ENGINE=INNODB;

CREATE TABLE respuestas_estudiante_evaluacion (
	id_estudiante int NOT NULL,
	id_pregunta int NOT NULL,
	id_respuesta int NOT NULL,
	id_evaluacion int NOT NULL,
	PRIMARY KEY (id_estudiante, id_pregunta, id_respuesta, id_evaluacion),
	FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id) ON DELETE CASCADE,
	FOREIGN KEY (id_pregunta) REFERENCES pregunta(id) ON DELETE CASCADE,
	FOREIGN KEY (id_respuesta) REFERENCES respuesta(id) ON DELETE CASCADE,
	FOREIGN KEY (id_evaluacion) REFERENCES evaluacion(id) ON DELETE CASCADE
) ENGINE=INNODB;


CREATE TABLE estudiante_evaluacion (
	id_estudiante int NOT NULL,
	id_evaluacion int NOT NULL,
	nota float NOT NULL,
	PRIMARY KEY (id_estudiante, id_evaluacion),
	FOREIGN KEY (id_estudiante) REFERENCES estudiantes(id) ON DELETE CASCADE,
	FOREIGN KEY (id_evaluacion) REFERENCES evaluacion(id) ON DELETE CASCADE
) ENGINE=INNODB;