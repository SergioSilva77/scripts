// Crie um evento de teclado para a tecla "Enter" (código 13)
var event = new KeyboardEvent('keydown', {
  key: 'Enter',
  code: 'Enter',
  which: 13,
  keyCode: 13,
  charCode: 13,
  bubbles: true,
  cancelable: true,
  composed: true
});

// Dispare o evento no documento ou em qualquer elemento desejado
document.dispatchEvent(event); // Dispara o evento no documento, mas você pode direcioná-lo para qualquer elemento desejado
