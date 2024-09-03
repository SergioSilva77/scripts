import MyComponent from "./MyComponents"

function Example07() {
    var arr = []
    for (let i = 0; i < 2; i++) {
        arr.push(<Example07 />)
    }
    return (
        <>
            {[...Array(10)].map((x, i) =>
                <MyComponent index={i} />
            )}
        </>
    )
}

export default Example07