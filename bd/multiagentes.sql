CREATE DATABASE IF NOT EXISTS 'multiagentes';
USE multiagentes;

CREATE TABLE `asignatura` (
  `id` int(6) UNSIGNED NOT NULL,
  `descripcion` varchar(200) NOT NULL
);


CREATE TABLE `estudiantes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `id_asignatura` int(11) NOT NULL
);


CREATE TABLE `evaluacion` (
  `id` int(6) UNSIGNED NOT NULL,
  `id_asignatura` int(6) DEFAULT NULL,
  `descripcion` varchar(200) NOT NULL,
  `simulacro` tinyint(1) DEFAULT '0',
  `nota` float DEFAULT NULL
);

CREATE TABLE `preguntas` (
  `id` int(6) UNSIGNED NOT NULL,
  `descripcion` varchar(200) NOT NULL
);


CREATE TABLE `preguntasxrespuesta` (
  `id_pregunta` int(6) UNSIGNED DEFAULT NULL,
  `id_respuesta` int(6) UNSIGNED DEFAULT NULL,
  `correcta` tinyint(1) DEFAULT '0'
);


CREATE TABLE `respuestas` (
  `id` int(6) UNSIGNED NOT NULL,
  `id_evaluacion` int(11) NOT NULL
);


CREATE TABLE `respuestasxevaluacion` (
  `id_respuestas` int(11) NOT NULL,
  `id_evaluacion` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `correctas` int(11) NOT NULL
);


ALTER TABLE `asignatura`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

ALTER TABLE `evaluacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id_asignatura` (`id_asignatura`);

ALTER TABLE `preguntas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`);

ALTER TABLE `preguntasxrespuesta`
  ADD KEY `id_pregunta` (`id_pregunta`),
  ADD KEY `id_respuesta` (`id_respuesta`);

ALTER TABLE `respuestas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `id` (`id`),
  ADD KEY `id_evaluacion` (`id_evaluacion`);

ALTER TABLE `respuestasxevaluacion`
  ADD KEY `id_respuestas` (`id_respuestas`),
  ADD KEY `id_evaluacion` (`id_evaluacion`);

ALTER TABLE `asignatura`
  MODIFY `id` int(6) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `evaluacion`
  MODIFY `id` int(6) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `preguntas`
  MODIFY `id` int(6) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `respuestas`
  MODIFY `id` int(6) UNSIGNED NOT NULL AUTO_INCREMENT;

ALTER TABLE `preguntasxrespuesta`
  ADD CONSTRAINT `preguntasxrespuesta_ibfk_1` FOREIGN KEY (`id_respuesta`) REFERENCES `respuestas` (`id`) ON UPDATE CASCADE,
  ADD CONSTRAINT `preguntasxrespuesta_ibfk_2` FOREIGN KEY (`id_pregunta`) REFERENCES `preguntas` (`id`) ON UPDATE CASCADE;

