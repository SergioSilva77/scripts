var link = document.createElement('a');
link.href = 'blob:https://verificadordiplomadigital.mec.gov.br/';
link.download = '{nome_xml}.pdf';
link.dispatchEvent(new MouseEvent('click'));