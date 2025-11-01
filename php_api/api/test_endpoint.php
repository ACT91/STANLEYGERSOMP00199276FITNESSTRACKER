<?php
// Simple test file to verify API is accessible
header('Content-Type: application/json');
header('Access-Control-Allow-Origin: *');

echo json_encode([
    'status' => 'success',
    'message' => 'API endpoint is reachable',
    'timestamp' => date('Y-m-d H:i:s'),
    'file' => __FILE__
]);

