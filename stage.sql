-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le : ven. 29 août 2025 à 13:02
-- Version du serveur : 10.4.32-MariaDB
-- Version de PHP : 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `stage`
--

-- --------------------------------------------------------

--
-- Structure de la table `files`
--

CREATE TABLE `files` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `filename` varchar(255) NOT NULL,
  `file_type` enum('pdf','word') NOT NULL,
  `path` varchar(255) NOT NULL,
  `uploaded_at` datetime DEFAULT current_timestamp(),
  `folder_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `files`
--

INSERT INTO `files` (`id`, `user_id`, `filename`, `file_type`, `path`, `uploaded_at`, `folder_id`) VALUES
(159, 22, 'ens (1).pdf', 'pdf', 'uploads\\148\\ens (1).pdf', '2025-08-29 11:21:32', 148),
(160, 22, 'ens (1).pdf', 'pdf', 'uploads\\149\\ens (1).pdf', '2025-08-29 11:26:26', 149),
(161, 22, 'Mona9achat majalis sayfiya (2) (1) (1) (1).docx', 'word', 'uploads\\150\\Mona9achat majalis sayfiya (2) (1) (1) (1).docx', '2025-08-29 11:26:53', 150);

-- --------------------------------------------------------

--
-- Structure de la table `folders`
--

CREATE TABLE `folders` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `folders`
--

INSERT INTO `folders` (`id`, `name`, `parent_id`) VALUES
(146, 'onda1', NULL),
(147, 'onda2', NULL),
(148, 'service2', 146),
(149, 'manuel des airodron', NULL),
(150, 'Anex1', 149),
(151, 'Anex2', 149),
(152, 'Anex3', 149);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(50) NOT NULL,
  `service` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `role`, `service`) VALUES
(22, 'said', '$2a$10$3iwrp3vSK/cNSuVC/eKgyeD5DvmpOQRxYGUu2OyfzrGTpYkFLE7He', 'ADMIN', 'ews'),
(56, 'said1', '$2a$10$gqHBRnBBaUFlL2O2HGEuqu6eRxZugjyyiZJTa7WOBogFAbEwJjXVG', 'CHEF', 'Service exploitation aerienne'),
(57, 'sa', '$2a$10$IgZscAzJNBfbHCum8iQdiOhltR8k.pOiDoo3EV7XdXn0gNg31Ec/q', 'CHEF', ''),
(58, 'saw', '$2a$10$o4RggoCY3XZD0A8UqZdfGOwahZi/HIe6lrREMCmaaJVWJFH87UPDC', 'CHEF', ''),
(59, 'omaronda', '$2a$10$nSfo6geQcheLnDAHqys1Uez5.BXQ6MLzrTUIrpDK7dLqopITZB2Ry', 'CHEF', 'Service Securite+qualite environnement'),
(60, 'om', '$2a$10$FJFDh/2G8VnaK1ozS5SwqukbZF1pOEPRmX8TXlJUvloYL79et3kI.', 'AGENT', 'Service exploitation aerienne');

-- --------------------------------------------------------

--
-- Structure de la table `user_folders`
--

CREATE TABLE `user_folders` (
  `user_id` bigint(20) NOT NULL,
  `folder_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Déchargement des données de la table `user_folders`
--

INSERT INTO `user_folders` (`user_id`, `folder_id`) VALUES
(59, 146),
(59, 148),
(60, 149),
(60, 150),
(60, 151),
(60, 152);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`id`),
  ADD KEY `files_ibfk_1` (`user_id`),
  ADD KEY `fk_folder` (`folder_id`);

--
-- Index pour la table `folders`
--
ALTER TABLE `folders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_parent_folder` (`parent_id`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Index pour la table `user_folders`
--
ALTER TABLE `user_folders`
  ADD PRIMARY KEY (`user_id`,`folder_id`),
  ADD KEY `fk_user_folders_folder` (`folder_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `files`
--
ALTER TABLE `files`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=162;

--
-- AUTO_INCREMENT pour la table `folders`
--
ALTER TABLE `folders`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=153;

--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `files`
--
ALTER TABLE `files`
  ADD CONSTRAINT `files_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_folder` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`id`) ON DELETE SET NULL;

--
-- Contraintes pour la table `folders`
--
ALTER TABLE `folders`
  ADD CONSTRAINT `fk_parent` FOREIGN KEY (`parent_id`) REFERENCES `folders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_parent_folder` FOREIGN KEY (`parent_id`) REFERENCES `folders` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `user_folders`
--
ALTER TABLE `user_folders`
  ADD CONSTRAINT `fk_user_folders_folder` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_folders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
