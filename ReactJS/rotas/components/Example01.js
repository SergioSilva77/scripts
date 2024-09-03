import styles from './Example01.module.css'

function Example01(props){
    return (
        <div className={styles.mycomponent}>
            <h1>Eai {props.nome} blzzz??</h1>
        </div>
    )
}

export default Example01