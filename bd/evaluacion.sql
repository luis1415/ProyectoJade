CREATE DATABASE IF NOT EXISTS 'multiagentes';
USE multiagentes;

CREATE TABLE asignatura (
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  	descripcion varchar(200) NOT NULL,

) ENGINE=InnoDB;


CREATE TABLE evaluacion (
  id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  id_asignatura INT(6),
  descripcion varchar(200) NOT NULL,
  simulacro boolean DEFAULT false,
  nota float NULL
) ENGINE=InnoDB;


CREATE TABLE preguntas (
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
  	descripcion varchar(200) NOT NULL,

) ENGINE=InnoDB;

CREATE TABLE respuestas (
	id INT(6) UNSIGNED AUTO_INCREMENT PRIMARY KEY,
	
) ENGINE=InnoDB;

CREATE TABLE preguntasXrespuesta (
	id_pregunta INT(6) UNSIGNED,
	id_respuesta INT(6) UNSIGNED,
	correcta boolean DEFAULT false,

) ENGINE=InnoDB;
