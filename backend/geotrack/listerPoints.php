<?php
header('Content-Type: application/json; charset=utf-8');

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['ok' => false, 'error' => 'Méthode non autorisée']);
    exit;
}

include_once __DIR__ . '/service/GeoPointDao.php';

try {
    $dao    = new GeoPointDao();
    $points = $dao->getAll();
    echo json_encode(['ok' => true, 'points' => $points]);
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(['ok' => false, 'error' => $e->getMessage()]);
}