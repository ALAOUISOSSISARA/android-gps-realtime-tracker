<?php
header('Content-Type: application/json; charset=utf-8');

// Sécurité : uniquement POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['ok' => false, 'error' => 'Méthode non autorisée']);
    exit;
}

include_once __DIR__ . '/service/GeoPointDao.php';
include_once __DIR__ . '/classe/GeoPoint.php';

// Récupération et nettoyage des paramètres
$latitude  = isset($_POST['latitude'])  ? trim($_POST['latitude'])  : null;
$longitude = isset($_POST['longitude']) ? trim($_POST['longitude']) : null;
$date      = isset($_POST['captured_at'])   ? trim($_POST['captured_at'])   : null;
$deviceId  = isset($_POST['device_id']) ? trim($_POST['device_id']) : null;

// Validation
if ($latitude === null || $longitude === null || $date === null || $deviceId === null) {
    http_response_code(400);
    echo json_encode(['ok' => false, 'error' => 'Paramètres manquants']);
    exit;
}

try {
    $dao   = new GeoPointDao();
    $point = new GeoPoint(null, $latitude, $longitude, $date, $deviceId);
    $dao->create($point);

    echo json_encode([
        'ok'      => true,
        'message' => 'Point enregistré avec succès',
        'ip'      => $_SERVER['REMOTE_ADDR']
    ]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['ok' => false, 'error' => $e->getMessage()]);
}