const Search = () => {
  const [showResults, setShowResults] = React.useState(false)
  const onClick = () => setShowResults(true)
  return (
    <div>
      <input type="submit" value="Search" onClick={onClick} />
      { showResults ? <Results /> : null }
    </div>
  )
}

const Results = () => (
  <div id="results" className="search-results">
    Some Results
  </div>
)

ReactDOM.render(<Search />, document.querySelector("#container"))


<script src="https://cdnjs.cloudflare.com/ajax/libs/react/16.13.1/umd/react.production.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/react-dom/16.13.1/umd/react-dom.production.min.js"></script>

<div id="container">
  <!-- This element's contents will be replaced with your component. -->
</div>