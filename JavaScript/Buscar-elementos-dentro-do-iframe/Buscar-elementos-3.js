var shadowRoot = document.querySelector('#my-element').shadowRoot;

var treeWalker = document.createTreeWalker(
  shadowRoot,
  NodeFilter.SHOW_ELEMENT,
  null,
  false
);

var node = treeWalker.nextNode();
while (node) {
  console.log(node);
  node = treeWalker.nextNode();
}