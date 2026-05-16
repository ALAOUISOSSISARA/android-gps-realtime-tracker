<?php
/**
 * Gère la connexion PDO à la base de données geotrack.
 */
class DbConnexion {
    private $pdo;

    public function __construct() {
        $host   = 'localhost';
        $dbname = 'geotrack';
        $user   = 'root';
        $pass   = '';

        try {
            $dsn = "mysql:host=$host;dbname=$dbname;charset=utf8";
            $this->pdo = new PDO($dsn, $user, $pass, [
                PDO::ATTR_ERRMODE            => PDO::ERRMODE_EXCEPTION,
                PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
                PDO::ATTR_EMULATE_PREPARES   => false,
            ]);
        } catch (PDOException $e) {
            http_response_code(500);
            die(json_encode(['ok' => false, 'error' => 'DB connection failed']));
        }
    }

    public function getPdo() {
        return $this->pdo;
    }
}