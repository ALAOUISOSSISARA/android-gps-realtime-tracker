<?php
/**
 * Modèle représentant un point GPS enregistré par un appareil.
 */
class GeoPoint {
    private $id;
    private $latitude;
    private $longitude;
    private $capturedAt;  // datetime MySQL
    private $deviceId;    // identifiant unique de l'appareil

    public function __construct($id, $latitude, $longitude, $capturedAt, $deviceId) {
        $this->id          = $id;
        $this->latitude    = $latitude;
        $this->longitude   = $longitude;
        $this->capturedAt  = $capturedAt;
        $this->deviceId    = $deviceId;
    }

    public function getId()         { return $this->id; }
    public function getLatitude()   { return $this->latitude; }
    public function getLongitude()  { return $this->longitude; }
    public function getCapturedAt() { return $this->capturedAt; }
    public function getDeviceId()   { return $this->deviceId; }

    public function setId($id)               { $this->id = $id; }
    public function setLatitude($v)          { $this->latitude = $v; }
    public function setLongitude($v)         { $this->longitude = $v; }
    public function setCapturedAt($v)        { $this->capturedAt = $v; }
    public function setDeviceId($v)          { $this->deviceId = $v; }
}