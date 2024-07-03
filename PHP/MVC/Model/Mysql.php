<?php
require_once (dirname ( __DIR__ ) . "/Config/Dbconfig.php");
class Mysql extends Dbconfig {
    private $sqlString;
    private $env;
    
    protected $databaseName;
    protected $hostName;
    protected $userName;
    protected $passCode;
    
    public $connectionString;
    public $dataSet;
    
    function __construct($env) {
        $this->env = $env;
    }
    
    function setMysql() {
        $this->connectionString = NULL;
        $this->sqlString = NULL;
        $this->dataSet = NULL;
        
        $dbPara = new Dbconfig($this->env);
        $this->databaseName = $dbPara->dbName;
        $this->hostName = $dbPara->serverName;
        $this->userName = $dbPara->userName;
        $this->passCode = $dbPara->passCode;
        $dbPara = NULL;
    }
    
    function dbConnect() {
        try {
            $this->connectionString = mysql_connect ( $this->hostName, $this->userName, $this->passCode );
            mysql_select_db ( $this->databaseName, $this->connectionString );
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
        }
        
        return $this->connectionString;
    }
    
    function dbDisconnect() {
        $this->connectionString = NULL;
        $this->sqlString = NULL;
        $this->dataSet = NULL;
        $this->databaseName = NULL;
        $this->hostName = NULL;
        $this->userName = NULL;
        $this->passCode = NULL;
    }
    
    function selectUser($user, $pass) {
        try {
            $this->sqlString = 'SELECT ID_USER, NM_USER FROM TRP_USER WHERE CD_LOGIN = "' . $user . '" AND CD_PASSW = "' . $pass . '"';
            $this->dataSet = mysql_query($this->sqlString, $this->connectionString );
			//$num_rows = mysql_num_rows($this->dataSet);
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
        }
        
        return $this->dataSet;
    }
    
    function executeQuery($sqlQuery) {
        try {
            $result = $this->select('', $sqlQuery);
            return $result;
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
        }
        
        return null;
    }
    
    function executeProc($procName, $procFilter) {
        try {
			$result = $this->select($procFilter,$procName);

            return $result;
        } catch ( Exception $e ) {
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
        }
        
        return null;
    }
    
    function select($filter, $sp) {
        $result = null;
        //echo $sp;
		//echo $filter[0];
		//echo $filter[1];
        
        try {
            
            $cmd = substr ( $sp, 0, 6 );
            if (strtoupper ($cmd) == 'SELECT') {
                $this->sqlString = $sp;  
            } else {
				if(is_array($filter)){		
					$query = "CALL ". $sp .'("'. implode('","', $filter) .'")';
					$this->sqlString = $query;
				}
				else{
				    $this->sqlString = 'CALL ' . $sp . '("' . $filter . '")';
				}
            }		   
			
		   // echo $this->sqlString;
            $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
            
            mysqli_query($conn,"SET NAMES 'utf8'");
            mysqli_query($conn,'SET character_set_connection=utf8');
            mysqli_query($conn,'SET character_set_client=utf8');
            mysqli_query($conn,'SET character_set_results=utf8');
            
            #$conn->set_charset("utf8");
            //echo $this->sqlString;
            $result = mysqli_query ( $conn, $this->sqlString );
            mysqli_close ( $conn );
        } catch ( Exception $e ) {
            
            echo 'Exce&ccedil;&atilde;o capturada: ', $e->getMessage (), "\n";
        }
        return $result;
    }

    function changeProtocol($arr){   
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {
                
                $this->sqlString = 'CALL spTRP_PROTOCOL_chg("' . $arr ['ID_PROTO'] . '","' . $arr ['DS_PROTO'] . '","' . $arr ['ID_USER'] . '","' . $arr ['flag'] . '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exce��o capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }

    function changePatient($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_PATIENT_chg("' . $arr ['ID_INSTI'] . '","' . $arr ['ID_PATIE'] . '","' . $arr ['ID_USER'] . '","' . 
				                                                $arr ['CD_IDENT_NATIO'] . '","' . $arr ['CD_IDENT_TAX'] . '","' . $arr ['CD_SEX'] . '","' . 
																$arr ['DT_BORN'] . '","' . $arr ['NM_PATIE'] . '","' . $arr ['NM_PATIE_SOCIA'] . '","' . 
																$arr ['QT_HEIGH'] . '","' . $arr ['QT_WEIGH'] . '","' . $arr ['DS_ADDRE'] . '","' . 
																$arr ['DS_ADDRE_COMPL'] . '","' . $arr ['DS_NEIGH'] . '","' . $arr ['DS_CITY'] . '","' . 
																$arr ['DS_STATE'] . '","' . $arr ['CD_POSTA'] . '","' . $arr ['NM_FATHE'] . '","' . 
																$arr ['NM_MOTHE'] . '","' .
                    											$arr ['flag'] . '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exce��o capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
	function changeInstitution($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_INSTITUTION_chg('.
											$arr ['ID_INSTI'] . ',' . $arr ['ID_USER'] . ',"' . $arr ['CD_IDENT_TAX'] . '","' . $arr ['CD_POSTA'] . '","' . 
											$arr ['CD_UF'] . '","' . $arr ['DS_CITY'] . '","' . $arr ['DS_STREE'] . '","' . $arr ['DS_COMPL'] . '","' . 
											$arr ['DS_NEIGH'] . '","' . $arr ['NM_INSTI'] . '","' . 
											$arr ['flag'] . '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA,@pID_INSTI_OUT); ';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exce��o capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
	function changeQuestionType($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_QUESTION_TYPE_chg("' . $arr ['ID_INSTI'] . '","' . $arr ['ID_QUEST_TYPE'] . '","' . $arr ['DS_QUEST_TYPE'] . '","' . 
				                                                      $arr ['FL_QUEST_OBSER'] . '","' . $arr ['ID_USER'] . '","' . $arr ['flag'] . 
																	  '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exceção capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
	function changeQuestionGroup($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_QUESTION_GROUP_chg("' . $arr ['ID_INSTI'] . '","' . $arr ['ID_QUEST_GROUP'] . '","' . $arr ['DS_QUEST_GROUP'] . '","' . 
				                                                       $arr ['ID_USER'] . '","' . $arr ['flag'] . 
																	  '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exceção capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
	function changeQuestion($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_QUESTION_chg("' . $arr['ID_INSTI'] . '","' . $arr['ID_QUEST'] . '","' . $arr['DS_QUEST'] . '","' . 
                                                                 $arr['ID_QUEST_PAREN'] . '","' . $arr['ID_QUEST_TYPE'] . '","' . $arr['ID_USER'] . '","' .  
                                                                 $arr['ID_QUEST_GROUP'] . '","' . $arr['DS_OPTI1'] . '","' . $arr['DS_OPTI2'] . '","' . 
																 $arr['DS_OPTI3'] . '","' . $arr['DS_OPTI4'] . '","' . $arr['DS_OPTI5'] . '","' . 
																 $arr['DS_OPTI6'] . '","' . $arr['DS_OPTI7'] . '","' . $arr['DS_OPTI8'] . '","' .
																 $arr ['flag'] . '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exceção capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }		
	
	function changeQuiz($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_QUIZ_chg("' . $arr ['ID_INSTI'] . '","' . $arr ['ID_QUIZ'] . '","' . $arr ['DS_QUIZ'] . '","' . 
				                                             $arr ['ID_USER'] . '","' .  $arr ['CL_QUIZ_QUEST'] . '","' . $arr ['flag'] . 
															 '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exceção capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
	function changeQuizCollection($arr){
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {		
                $this->sqlString = 'CALL spTRP_QUIZ_QUESTION_ANSWER_chg("' . $arr ['ID_CONTR'] . '","' . $arr ['VL_CONTR'] . '","' . $arr ['flag'] . 
															 '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA;';               
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2]
                                );
                            }
                            
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Exceção capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
    function changeUser($arr) {
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {
                
                $this->sqlString = 'CALL spTRP_USER_chg("' . $arr ['ID_HOSPI'] . '","' . $arr ['ID_USER'] . '","' . $arr ['CD_LOGIN'] . '","' . $arr ['NM_USER'] . '","' . $arr ['DT_EXPIR'] . '","' . $arr ['DS_USER'] . '","' . $arr ['CD_PASSW'] . '","' . $arr ['flag'] . '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA,@pID_USER_NEW );';
                
                //var_dump($this -> sqlString);
                
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA, @pID_USER_NEW;';
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2],
								"pID_USER_NEW" =>  $row [3]
                                );
                            }

                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Excecao capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	
	
	function changeFeature($arr) {
        try {
            for($i = 0; $i <= count ( $arr ); $i ++) {
                
                $this->sqlString = 'CALL spTRP_FEATURE_chg("' . $arr ['ID_MENU'] . '","' . $arr ['NM_MENU'] . '","' . $arr ['DS_URL'] . '","' . $arr ['ID_MENU_PAREN'] . '","' . $arr ['ID_ORDER'] . '","' . $arr ['NM_ALIAS'] . '","' . $arr ['ID_MENU_LOG'] . '","' . $arr ['NR_GRID_RECOR'] . '","' .$arr ['flag'] . '","' .$arr ['ID_USER'] . '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA);';
                
                //var_dump($this -> sqlString);
                
                $this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA, @pID_USER_NEW;';
  
                $conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
                mysqli_multi_query($conn,"SET NAMES 'utf8'");
                mysqli_multi_query($conn,'SET character_set_connection=utf8');
                mysqli_multi_query($conn,'SET character_set_client=utf8');
                mysqli_multi_query($conn,'SET character_set_results=utf8');
                
                if (mysqli_multi_query ( $conn, $this->sqlString )) {
                    do {
                        if ($resultado = mysqli_store_result ( $conn )) {
                            while ( $row = mysqli_fetch_row ( $resultado ) ) {
                                $arr = array (
                                "pCD_MESSA" => $row [0],
                                "pDS_MESSA" => $row [1],
                                "pTP_MESSA" => $row [2],
								"pID_USER_NEW" =>  $row [3]
                                );
                            }
							
                            mysqli_free_result ( $resultado );
                        }
                    } while ( mysqli_next_result ( $conn ) );
                    mysqli_close ( $conn );
                    
                    return $arr;
                } else {
                    echo $this->sqlString . 'Erro MySQL: ' . mysqli_error ( $conn ) . '</p>';
                    exit ();
                    mysqli_close ( $conn );
                }
            }
            
            return $arr;
        } catch ( Exception $e ) {
            echo 'Excecao capturada: ', $e->getMessage (), '\n';
            return $arr;
        }
    }	

	function checkUser($arr) {
	try {
		$this->sqlString = 'CALL spTRP_USER_CHECK_get("' . $arr ['CD_LOGIN'] . '","' . $arr ['CD_PASSW'] . '", @pCD_MESSA, @pDS_MESSA, @pTP_MESSA, @pID_USER_NEW );';         
		$this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA, @pID_USER_NEW;';
    
		$conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
		mysqli_multi_query($conn,"SET NAMES 'utf8'");
		mysqli_multi_query($conn,'SET character_set_connection=utf8');
		mysqli_multi_query($conn,'SET character_set_client=utf8');
		mysqli_multi_query($conn,'SET character_set_results=utf8');
                
		if (mysqli_multi_query ( $conn, $this->sqlString )) {
			do {
				if ($resultado = mysqli_store_result ( $conn )) {
					while ( $row = mysqli_fetch_row ( $resultado ) ) {
						$arr = array ("pCD_MESSA" => $row [0],
									  "pDS_MESSA" => $row [1],
                                      "pTP_MESSA" => $row [2],
								      "pID_USER" => $row [3]);
					}
							
					mysqli_free_result ( $resultado );
				}
			} while (mysqli_next_result ($conn));
        
			mysqli_close ( $conn );
			
			//return $arr;
			
        } else {
			echo $this->sqlString . 'Error MySQL: ' . mysqli_error ( $conn ) . '</p>';
            exit ();
            mysqli_close ( $conn );
        }	
		
		return $arr;
	} catch ( Exception $e ) {
		echo 'Excecao capturada: ', $e->getMessage (), '\n';
		return $arr;
	}
	}
	
	function changeScheduler($arr) {
		try {
			$this->sqlString = 'CALL spTRP_SCHEDULER_chg("' . $arr ['ID_SCHED'] . '",' . $arr ['ID_PATIE'] . ',' . $arr ['ID_SCHED_TYPE'] . ',' .
			                                           '"' . $arr ['DS_SCHED'] . '","' . $arr ['DT_SCHED_FINIS'] . '","' . $arr ['DT_SCHED_START'] . '",' .
													   '"' . $arr ['NM_SCHED'] . '","' . $arr ['ID_USER'] . '","' . $arr ['flag'] .
													   '",@pCD_MESSA, @pDS_MESSA, @pTP_MESSA );';
			$this->sqlString .= 'SELECT @pCD_MESSA, @pDS_MESSA, @pTP_MESSA, @pID_USER_NEW;';
    
			$conn = mysqli_connect ( $this->hostName, $this->userName, $this->passCode, $this->databaseName );
			mysqli_multi_query($conn,"SET NAMES 'utf8'");
			mysqli_multi_query($conn,'SET character_set_connection=utf8');
			mysqli_multi_query($conn,'SET character_set_client=utf8');
			mysqli_multi_query($conn,'SET character_set_results=utf8');
                
			if (mysqli_multi_query ( $conn, $this->sqlString )) {
				do {
					if ($resultado = mysqli_store_result ( $conn )) {
						while ( $row = mysqli_fetch_row ( $resultado ) ) {
							$arr = array ("pCD_MESSA" => $row [0],
										"pDS_MESSA" => $row [1],
										"pTP_MESSA" => $row [2],
										"pID_USER" => $row [3]);
						}
							
						mysqli_free_result ( $resultado );
					}
				} while (mysqli_next_result ($conn));
        
				mysqli_close ( $conn );
			
				//return $arr;
			} else {
				echo $this->sqlString . 'Error MySQL: ' . mysqli_error ( $conn ) . '</p>';
				exit ();
				mysqli_close ( $conn );
			}	
		
			return $arr;
		} catch ( Exception $e ) {
			echo 'Excecao capturada: ', $e->getMessage (), '\n';
			return $arr;
		}
	}	
}
?>