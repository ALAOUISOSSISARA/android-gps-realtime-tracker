<?php
include_once __DIR__ . '/../dao/IDao.php';
include_once __DIR__ . '/../classe/GeoPoint.php';
include_once __DIR__ . '/../connexion/DbConnexion.php';

/**
 * Accès aux données pour la table geo_point.
 */
class GeoPointDao implements IDao {
    private $pdo;

    public function __construct() {
        $db        = new DbConnexion();
        $this->pdo = $db->getPdo();
    }

    /** Insère un nouveau point GPS en base. */
    public function create($point) {
        $sql  = "INSERT INTO geo_point (latitude, longitude, captured_at, device_id)
                 VALUES (:lat, :lon, :captured_at, :device_id)";
        $stmt = $this->pdo->prepare($sql);
        $stmt->execute([
            ':lat'         => $point->getLatitude(),
            ':lon'         => $point->getLongitude(),
            ':captured_at' => $point->getCapturedAt(),
            ':device_id'   => $point->getDeviceId(),
        ]);
        return true;
    }

    /** Retourne tous les points GPS enregistrés. */
    public function getAll() {
        $stmt = $this->pdo->prepare("SELECT * FROM geo_point ORDER BY captured_at DESC");
        $stmt->execute();
        return $stmt->fetchAll();
    }

    public function update($obj)   { /* non utilisé */ }
    public function delete($id)    { /* non utilisé */ }
    public function getById($id)   { /* non utilisé */ }
}