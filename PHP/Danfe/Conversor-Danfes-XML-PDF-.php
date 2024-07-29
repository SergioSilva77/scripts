<?php

namespace teste;

require 'C:/Users/sserg/OneDrive/Documentos/Dev/php/sped-da/vendor/autoload.php';

use NFePHP\DA\NFe\Danfe;

header('Content-Type: application/json');

function respond($status, $message) {
    echo json_encode(['status' => $status, 'message' => $message]);
    exit();
}

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    respond('error', 'Invalid request method.');
}

$data = json_decode(file_get_contents('php://input'), true);

if (!isset($data['xmlKey']) || !isset($data['caminhoPdf']) || !isset($data['xmlContent'])) {
    respond('error', 'Missing required parameters.');
}

$xmlKey = $data['xmlKey'];
$caminhoPdf = $data['caminhoPdf'];
$xmlContent = $data['xmlContent'];

class PDFGenerator {
    public function generatePDF($xmlKey, $caminhoPdf, $xmlContent) {
        $danfe = new Danfe($xmlContent);
        $pdfContent = $danfe->render();

        // $pdfPath = $caminhoPdf . '/' . $xmlKey . '.pdf';

        // if (!is_dir($caminhoPdf)) {
        //     mkdir($caminhoPdf, 0777, true);
        // }

        // file_put_contents($pdfPath, $pdfContent);

        return $pdfContent;
    }
}

$pdfGenerator = new PDFGenerator();
$pdfContent = $pdfGenerator->generatePDF($xmlKey, $caminhoPdf, $xmlContent);

// Set headers to return the PDF as a binary stream
header('Content-Type: application/pdf');
header('Content-Disposition: inline; filename="' . $xmlKey . '.pdf"');
header('Content-Length: ' . strlen($pdfContent));

echo $pdfContent;
?>
