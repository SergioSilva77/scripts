<?php
if(session_id() == '') {
    session_start();
}
require_once('../Model/Model.php');

class Controller{
    
    //function __construct() {
        #session_start();
        #print 'function __construct() = '.$PHPSESSID;
    //}
    
    public function action($var){
        $model = new Model();
        $msg = '';

        if($var == 'login'){
            if($_POST['action'] == 'login' and isset($_POST['user']) and isset($_POST['pass'])){
                $_SESSION['logado'] = '0';
                $result = $model->checkUser($_POST['user'], $_POST['pass']);
                $isUser = false;
                
                while($r = mysql_fetch_assoc($result)) {
					var_dump($result);
                    $isUser = true;
                    $_SESSION['logado'] = '1';
                    $_POST['usr_id'] = $r["usr_id"];
                    $_POST['usr_nome'] = $r["usr_nome"];
					$_SESSION['_LOG_USER_'] = $r["ID_USER"];
                }
                
                if (isset($result) and $isUser) {
                    $_SESSION['logado'] = '1';
                    $result = null;
                    $_POST['user'] = null;
                    $_POST['pass'] = null;
                    $_POST['action'] = null;
                    $_SESSION['arr'] = '';
                    $_SESSION['msg'] = '';
                    
					header("Location: ../indexTherappy.html");
                    exit();
                    
                }else{
					header ( "Location: /Therappy/indexTherappy.php" );
					
					echo '<script language="javascript">';
					echo 'alert("Usuário Inexistente !")';
					echo '</script>';		
				}
			}    
		}else{
			echo '<script language="javascript">';
			echo 'alert("Preencha Usuário e Senha !")';
			echo '</script>';
        }
    }
}