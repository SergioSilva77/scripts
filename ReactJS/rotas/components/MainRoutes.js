import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom'
import Empresa from '../pages/Empresa'
import Home from '../pages/Home'
import Contato from '../pages/Contato'
import Sobre from '../pages/Sobre'
import Footer from './Footer'

function MainRoutes() {
    
    return (<>
        <Router>
            <ul>
                <li><Link to='/'>Home</Link></li>
                <li><Link to='/empresa'>Empresa</Link></li>
                <li><Link to='/contato'>Contato</Link></li>
                <li><Link to='/sobre'>Sobre</Link></li>
            </ul>
            <Routes>
                <Route  path='/' element={<Home />}/>
                <Route  path='/empresa' element={<Empresa />}/>
                <Route  path='/contato' element={<Contato />}/>
                <Route  path='/sobre' element={<Sobre />}/>                
            </Routes>
            <Footer/>
        </Router>
    </>)
}

export default MainRoutes