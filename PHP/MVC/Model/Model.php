<?php
require_once (__DIR__ . '/Mysql.php');
class Model {


    function __construct() {     
       
    }

    public function checkUser($name, $pass) {
        try {
            $sql = new Mysql('');
            $sql->setMysql();
            $sql->dbConnect();
            
            $resultado = $sql->selectUser($name, $pass);	
            if (!$resultado) {
                throw new Exception ('Database error: '.mysql_error());
            }        
			
            $sql->dbDisconnect ();
            unset($sql);
            return $resultado;
			
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
            return NULL;
        }
    }
        
    public function loadProc($filter, $sp) {
        try {
            $sql = new Mysql ( '' );
            $sql->setMysql ();
            
            $result = $sql->select ( $filter, $sp );
            
            $array_jason = array ();
            for($i = mysqli_num_rows ( $result ) - 1; $i >= 0; $i --) {
                if (! mysqli_data_seek ( $result, $i )) {
                    echo "Não foi poss�vel mover para a linha $i: " . mysql_error () . "\n";
                    continue;
                }
                
                if (($row = mysqli_fetch_array ( $result, MYSQL_ASSOC ))) {
                    $fixed = array ();
                    foreach ( $row as $key => $value ) {
                        $fixed [$key] = $value;//utf8_encode ( $value );
                    }
                    $fixed ['SEL'] = FALSE;
                    $array_jason [$i] = json_encode ( $fixed );
                }
            }
            
            if (! $result) {
                throw new Exception ( 'Database error: ' . mysql_error () );
            }
            
            $sql->dbDisconnect();
            $result = null;
            
            return $array_jason;
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
            $result = null;
            return NULL;
        }
    }
    
    public function loadProc2($filter, $sp) {
        try {
            $sql = new Mysql ( '' );
            $sql->setMysql ();

            $result = $sql->select ( $filter, $sp );
			$resultData = array();
            
            while ($array = mysqli_fetch_assoc($result)) {
                $resultData[] = $array;
            }

            if (! $result) {
                throw new Exception ( 'Database error: ' . mysql_error () );
            }
            /*if( mysqli_num_rows ( $result ) > 0) {
                while ($array = mysqli_fetch_assoc($result)) {
                    $resultData[] = $array;
                }
				
            }else
                
            {
                //$sql->dbDisconnect ();
                //throw new Exception ( 'Database error: ' . mysql_error () );
                $resultData = array("message" => "Falha na comunicação com o banco de dados ");
            }*/
            
            $sql->dbDisconnect();
            $result = null;
            return $resultData;
            
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
            $result = null;
            return NULL;
        }
    }
    
    
}