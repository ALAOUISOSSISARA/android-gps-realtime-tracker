<?php
/**
 * Interface CRUD générique.
 */
interface IDao {
    public function create($obj);
    public function update($obj);
    public function delete($id);
    public function getById($id);
    public function getAll();
}