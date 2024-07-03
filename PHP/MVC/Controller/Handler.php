<?php
if(session_id() == '') {
    session_start();
}

#echo "IdUsuario: ". $idUser; 
#exit();
$idUsuario = $_SESSION['_LOG_USER_'];
$idInsti = $_SESSION['_ID_INSTI_'];

require_once ('Controller.php');
require_once ('../Model/Mysql.php');
require_once ('../Model/Model.php');

$controller = new Controller();
$model = new Model();

if(isset($_GET["pg"])){
	if ($_GET ['pg'] == '101') {
        $_SESSION ['arr'] = $model->loadProc ( '', 'spTRP_PROTOCOL_get' );
        header ( "Location: /Therappy/mainTRPProtocol.php" );
        exit();
	} else if ($_GET ['pg'] == '102') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_PATIENT_get' );
        header ( "Location: /Therappy/mainTRPPatient.php" );
        exit();
	} else if ($_GET ['pg'] == '103') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_INSTITUTION_get' );
        header ( "Location: /Therappy/mainTRPInstitution.php" );
        exit();
    } else if ($_GET ['pg'] == '201') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_TYPE_get' );
        header ( "Location: /Therappy/mainTRPQuestionType.php" );
        exit();	
    } else if ($_GET ['pg'] == '202') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_get' );
        header ( "Location: /Therappy/mainTRPQuestion.php" );
        exit();
    } else if ($_GET ['pg'] == '203') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUIZ_get' );
        header ( "Location: /Therappy/mainTRPQuiz.php" );
        exit();			
	} else if ($_GET ['pg'] == '204') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_GROUP_get' );		
        header ( "Location: /Therappy/mainTRPQuestionGroup.php" );
        exit ();
	} else if ($_GET ['pg'] == '206') {
        $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUIZ_QUESTION_COLLECTION_get' );		
        header ( "Location: /Therappy/mainTRPQuizCollection.php" );
        exit ();
	} else if ($_GET ['pg'] == '300') {
        $_SESSION ['arr'] = $model->loadProc ('','spTRP_SCHEDULER_get' );		
        header ( "Location: /Therappy/mainTRPScheduler.php" );
        exit ();		
    }else if ($_GET ['pg'] == '501') {
        $_SESSION ['arr'] = $model->loadProc ( '', 'spTRP_USER_get' );    
        header ( "Location: /Therappy/mainTRPUser.php" );
        exit();		
	} else if ($_GET ['pg'] == '502') {
		$_SESSION ['arr'] = $model->loadProc ( '', 'spTRP_FEATURE_get' );
		header ( "Location: ../mainTRPMenu.php" );
		exit ();	
	} else if ($_GET ['pg'] == '205') {
		$_SESSION ['arr'] = $model->loadProc ('', 'spTRP_RPT_QUIZ_get' );
        header ( "Location: /Therappy/rptTRP_Quiz.php" );
        exit ();		
	}
}

if (isset ( $_POST ['action'] ) or isset ( $_GET ['action'] )) {
    if ($_POST ['action'] == 'login') {
		$controller->action('login');
	
	// ## PROTOCOL - 101 ###
    } else if ($_POST ['action'] == 'actProtocol' or $_GET ['action'] == 'actProtocol') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ( $_POST ['search'], 'spTRP_PROTOCOL_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPProtocol.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
                'ID_PROTO' => $_GET ['ID_PROTO'],
                'DS_PROTO' => $_GET ['DS_PROTO'],
				'ID_USER' => $idUsuario,
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
                'ID_PROTO' => '',
                'DS_PROTO' => $_GET ['DS_PROTO'],
				'ID_USER' => $idUsuario,
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
                'ID_PROTO' => $_GET ['ID_PROTO'],
                'DS_PROTO' => '',
                'ID_USER' => $idUsuario,
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
            
            try {
                header('Content-Type: text/html; charset=utf-8');
                
                $sql = new Mysql ( '' );
                $sql->setMysql ();
                
                $result = $sql->changeProtocol($arr);
                
                for($i = 0; $i <= count ( $result ); $i ++) {
                    $jsonArr [$i] = json_encode ( $result );
                }
                
                if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
                    $_SESSION ['msg'] = '';
                } else {
                    $_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
                }
            } catch ( Exception $e ) {
                echo 'Exce&ccedil;&atilde;o capturada actProtocol: ', $e->getMessage (), "\n";
                $result = null;
                $jsonArr = null;
            }
            
            $sql->dbDisconnect ();
            $sql = null;
            
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ( $_POST ['search'], 'spTRP_PROTOCOL_get' );
            
            header ( "Location: /Therappy/mainTRPProtocol.php" );
            exit ();
        }		
    
	// ## PATIENT - 102 ###
    } else if ($_POST ['action'] == 'actPatient' or $_GET ['action'] == 'actPatient') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_PATIENT_get');
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPPatient.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
                'ID_PATIE' => $_GET ['ID_PATIE'],
                'NM_PATIE' => $_GET ['NM_PATIE'],
				'DT_BORN' => $_GET ['DT_BORN'],
				'QT_HEIGH' => $_GET ['QT_HEIGH'],
                'QT_WEIGH' => $_GET ['QT_WEIGH'],				
                'CD_SEX' => $_GET ['CD_SEX'],
                'CD_IDENT_NATIO' => $_GET ['CD_IDENT_NATIO'],
                'CD_IDENT_TAX' => $_GET ['CD_IDENT_TAX'],
				'NM_PATIE_SOCIA' => $_GET ['NM_PATIE_SOCIA'],
				'DS_ADDRE' => $_GET ['DS_ADDRE'],
				'DS_ADDRE_COMPL' => $_GET ['DS_ADDRE_COMPL'],
				'DS_NEIGH' => $_GET ['DS_NEIGH'],
				'DS_CITY' => $_GET ['DS_CITY'],
				'DS_STATE' => $_GET ['DS_STATE'],
				'CD_POSTA' => $_GET ['CD_POSTA'],
				'NM_FATHE' => $_GET ['NM_FATHE'],
				'NM_MOTHE' => $_GET ['NM_MOTHE'],
				'ID_USER' => $idUsuario,
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (		
                'ID_PATIE' => '',
                'NM_PATIE' => $_GET ['NM_PATIE'],
				'DT_BORN' => $_GET ['DT_BORN'],
				'QT_HEIGH' => $_GET ['QT_HEIGH'],
                'QT_WEIGH' => $_GET ['QT_WEIGH'],				
                'CD_SEX' => $_GET ['CD_SEX'],
                'CD_IDENT_NATIO' => $_GET ['CD_IDENT_NATIO'],
                'CD_IDENT_TAX' => $_GET ['CD_IDENT_TAX'],
                'NM_PATIE_SOCIA' => $_GET ['NM_PATIE_SOCIA'],	
				'DS_ADDRE' => $_GET ['DS_ADDRE'],
				'DS_ADDRE_COMPL' => $_GET ['DS_ADDRE_COMPL'],
				'DS_NEIGH' => $_GET ['DS_NEIGH'],
				'DS_CITY' => $_GET ['DS_CITY'],
				'DS_STATE' => $_GET ['DS_STATE'],
				'CD_POSTA' => $_GET ['CD_POSTA'],
				'NM_FATHE' => $_GET ['NM_FATHE'],
				'NM_MOTHE' => $_GET ['NM_MOTHE'],				
				'ID_USER' => $idUsuario,
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
                'ID_PATIE' => $_GET ['ID_PATIE'],
                'NM_PATIE' => '',
				'DT_BORN' => '',
				'QT_HEIGH' => '',
                'QT_WEIGH' => '',				
                'CD_SEX' => '',
                'CD_IDENT_NATIO' => '',
                'CD_IDENT_TAX' => '',
                'NM_PATIE_SOCIA' => '',	
				'DS_ADDRE' => '',
				'DS_ADDRE_COMPL' => '',
				'DS_NEIGH' => '',
				'DS_CITY' => '',
				'DS_STATE' => '',
				'CD_POSTA' => '',
				'NM_FATHE' => '',
				'NM_MOTHE' => '',
                'ID_USER' => $idUsuario,
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
            
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
				try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changePatient($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actProtocol: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_PATIENT_get' );
            
            header ( "Location: /Therappy/mainTRPPatient.php" );
            exit ();
            }		

	// CLINICA - 103 ###
	} else if ($_POST ['action'] == 'actInsti' or $_GET ['action'] == 'actInsti') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_INSTITUTION_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPInstitution.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
				'ID_INSTI' => $_GET ['ID_INSTI'],
				'ID_USER' => $idUsuario,
				'CD_IDENT_TAX' => $_GET ['CD_IDENT_TAX'],
				'CD_POSTA' => $_GET ['CD_POSTA'],
				'CD_UF' => $_GET ['CD_UF'],				
				'DS_CITY' => $_GET ['DS_CITY'],
				'DS_STREE' => $_GET ['DS_STREE'],
				'DS_COMPL' => $_GET ['DS_COMPL'],
				'DS_NEIGH' => $_GET ['DS_NEIGH'],
				'NM_INSTI' => $_GET ['NM_INSTI'],
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
				'ID_INSTI' => '',
				'ID_USER' => $idUsuario,
				'CD_IDENT_TAX' => $_GET ['CD_IDENT_TAX'],
				'CD_POSTA' => $_GET ['CD_POSTA'],
				'CD_UF' => $_GET ['CD_UF'],				
				'DS_CITY' => $_GET ['DS_CITY'],
				'DS_STREE' => $_GET ['DS_STREE'],
				'DS_COMPL' => $_GET ['DS_COMPL'],
				'DS_NEIGH' => $_GET ['DS_NEIGH'],
				'NM_INSTI' => $_GET ['NM_INSTI'],
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
				'ID_INSTI' => $_GET ['ID_INSTI'],
				'ID_USER' =>  $idUsuario,	
				'CD_IDENT_TAX' => '',
				'CD_POSTA' => '',
				'CD_UF' => '',				
				'DS_CITY' => '',
				'DS_STREE' => '',
				'DS_COMPL' => '',
				'DS_NEIGH' => '',
				'NM_INSTI' => '',
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changeInstitution($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actInsti: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_INSTITUTION_get' );
            
            header ( "Location: /Therappy/mainTRPInstitution.php" );
            exit ();
        }

	// QUESTION TYPE - 201 ###
	} else if ($_POST ['action'] == 'actQuestionType' or $_GET ['action'] == 'actQuestionType') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_TYPE_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPQuestionType.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST_TYPE' => $_GET ['ID_QUEST_TYPE'],
				'DS_QUEST_TYPE' => $_GET ['DS_QUEST_TYPE'],
				'FL_QUEST_OBSER' => $_GET ['FL_QUEST_OBSER'],			
				'ID_USER' => $idUsuario,
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST_TYPE' => '',
				'DS_QUEST_TYPE' => $_GET ['DS_QUEST_TYPE'],
				'FL_QUEST_OBSER' => $_GET ['FL_QUEST_OBSER'],			
				'ID_USER' => $idUsuario,
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST_TYPE' => '',
				'DS_QUEST_TYPE' => '',
				'FL_QUEST_OBSER' => '',			
				'ID_USER' => $idUsuario,
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changeQuestionType($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actQuestionType: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_TYPE_get' );
            
            header ( "Location: /Therappy/mainTRPQuestionType.php" );
            exit ();
        }

	// QUIZ - 202 ###
	} else if ($_POST ['action'] == 'actQuestion' or $_GET ['action'] == 'actQuestion') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPQuestion.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST' => $_GET ['ID_QUEST'],
				'DS_QUEST' => $_GET ['DS_QUEST'],
				'ID_QUEST_PAREN' => $_GET ['ID_QUEST_PAREN'],
				'ID_QUEST_TYPE' => $_GET ['ID_QUEST_TYPE'],
				'ID_USER' => $idUsuario,
				'ID_QUEST_GROUP' => $_GET ['ID_QUEST_GROUP'],
				'DS_OPTI1' => $_GET ['DS_OPTI1'], 
				'DS_OPTI2' => $_GET ['DS_OPTI2'], 
				'DS_OPTI3' => $_GET ['DS_OPTI3'], 
				'DS_OPTI4' => $_GET ['DS_OPTI4'], 
				'DS_OPTI5' => $_GET ['DS_OPTI5'], 
				'DS_OPTI6' => $_GET ['DS_OPTI6'], 
				'DS_OPTI7' => $_GET ['DS_OPTI7'], 
				'DS_OPTI8' => $_GET ['DS_OPTI8'],
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST' => '',
				'DS_QUEST' => $_GET ['DS_QUEST'],
				'ID_QUEST_PAREN' => $_GET ['ID_QUEST_PAREN'],
				'ID_QUEST_TYPE' => $_GET ['ID_QUEST_TYPE'],
				'ID_USER' => $idUsuario,
				'ID_QUEST_GROUP' => $_GET ['ID_QUEST_GROUP'],
				'DS_OPTI1' => $_GET ['DS_OPTI1'], 
				'DS_OPTI2' => $_GET ['DS_OPTI2'], 
				'DS_OPTI3' => $_GET ['DS_OPTI3'], 
				'DS_OPTI4' => $_GET ['DS_OPTI4'], 
				'DS_OPTI5' => $_GET ['DS_OPTI5'], 
				'DS_OPTI6' => $_GET ['DS_OPTI6'], 
				'DS_OPTI7' => $_GET ['DS_OPTI7'], 
				'DS_OPTI8' => $_GET ['DS_OPTI8'],				
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST' => $_GET ['ID_QUEST'],
				'DS_QUEST' => '',
				'ID_QUEST_PAREN' => '',
				'ID_QUEST_TYPE' => '',
				'ID_USER' => $idUsuario,
				'ID_QUEST_GROUP' => '',	
				'DS_OPTI1' => '', 
				'DS_OPTI2' => '', 
				'DS_OPTI3' => '', 
				'DS_OPTI4' => '', 
				'DS_OPTI5' => '', 
				'DS_OPTI6' => '', 
				'DS_OPTI7' => '', 
				'DS_OPTI8' => '',
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changeQuestion($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actQuiz: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_get' );
            
            header ( "Location: /Therappy/mainTRPQuestion.php" );
            exit ();
        }
		
	// QUIZ - 203 ###
	} else if ($_POST ['action'] == 'actQuiz' or $_GET ['action'] == 'actQuiz') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUIZ_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPQuiz.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUIZ' => $_GET ['ID_QUIZ'],
				'DS_QUIZ' => $_GET ['DS_QUIZ'],
				'CL_QUIZ_QUEST' => $_GET['selectedQuestionsList'],
				'ID_USER' => $idUsuario,
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
				'ID_INSTI' => $idInsti,
				'ID_QUIZ' => '',
				'DS_QUIZ' => $_GET ['DS_QUIZ'],
				'CL_QUIZ_QUEST' => $_GET['selectedQuestionsList'],				
				'ID_USER' => $idUsuario,
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUIZ' => $_GET ['ID_QUIZ'],
				'DS_QUIZ' => '',
				'CL_QUIZ_QUEST' => '',				
				'ID_USER' => $idUsuario,
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changeQuiz($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actQuiz: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUIZ_get' );
            
            header ( "Location: /Therappy/mainTRPQuiz.php" );
            exit ();
        }

    // QUESTION GROUP - 204 ###
	} else if ($_POST ['action'] == 'actQuestionGroup' or $_GET ['action'] == 'actQuestionGroup') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_GROUP_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPQuestionGroup.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST_GROUP' => $_GET ['ID_QUEST_GROUP'],
				'DS_QUEST_GROUP' => $_GET ['DS_QUEST_GROUP'],	
				'ID_USER' => $idUsuario,
                'flag' => 'U'
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST_GROUP' => '',
				'DS_QUEST_GROUP' => $_GET ['DS_QUEST_GROUP'],
				'ID_USER' => $idUsuario,
                'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
				'ID_INSTI' => $idInsti,	
				'ID_QUEST_GROUP' => '',
				'DS_QUEST_GROUP' => '',
				'ID_USER' => $idUsuario,
                'flag' => 'D'
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changeQuestionGroup($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actQuestionGroup: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUESTION_GROUP_get' );
            
            header ( "Location: /Therappy/mainTRPQuestionGroup.php" );
            exit ();
        }
		
    // QUESTION GROUP - 300 ###
	} else if ($_POST ['action'] == 'actScheduler' or $_GET ['action'] == 'actScheduler') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_SCHEDULER_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPScheduler.php" );
            exit ();
        } else {
            $arr = array ();
			
            if ($_GET ['crud'] == 'U') {
                $arr = array (
                'ID_SCHED' => $_GET ['ID_SCHED'],
                'ID_PATIE' => $_GET ['ID_PATIE'],
                'ID_SCHED_TYPE' => $_GET ['ID_SCHED_TYPE'],
                'DS_SCHED' => $_GET ['DS_SCHED'],
                'DT_SCHED_FINIS' => $_GET ['DT_SCHED_FINIS'],
                'DT_SCHED_START' => $_GET ['DT_SCHED_START'],
                'NM_SCHED' => $_GET ['NM_SCHED'],
				'ID_USER' => $idUsuario,
                'flag' => 'U'
                );
               
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
                    'ID_SCHED' => '',
                    'ID_PATIE' => $_GET ['ID_PATIE'],
                    'ID_SCHED_TYPE' => $_GET ['ID_SCHED_TYPE'],
                    'DS_SCHED' => $_GET ['DS_SCHED'],
                    'DT_SCHED_FINIS' => $_GET ['DT_SCHED_FINIS'],
                    'DT_SCHED_START' => $_GET ['DT_SCHED_START'],
                    'NM_SCHED' => $_GET ['NM_SCHED'],
                    'ID_USER' => $idUsuario,
                    'flag' => 'I'
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
                    'ID_SCHED' => $_GET ['ID_SCHED'],
                    'ID_PATIE' => $_GET ['ID_PATIE'],
                    'ID_SCHED_TYPE' => $_GET ['ID_SCHED_TYPE'],
                    'DS_SCHED' => $_GET ['DS_SCHED'],
                    'DT_SCHED_FINIS' => $_GET ['DT_SCHED_FINIS'],
                    'DT_SCHED_START' => $_GET ['DT_SCHED_START'],
                    'NM_SCHED' => $_GET ['NM_SCHED'],
                    'ID_USER' => $idUsuario,
                    'flag' => 'D'
                );
            }

            // echo print_r($arr);
            // exit();
            
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
                
					$result = $sql->changeScheduler($arr);
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actQuestionGroup: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_SCHEDULER_get' );
            
            header ( "Location: /Therappy/mainTRPScheduler.php?msg=".$result['pDS_MESSA'] );
            exit ();
        }		
		
    // QUESTION GROUP - 206 ###
	} else if ($_POST ['action'] == 'actQuizCollection' or $_GET ['action'] == 'actQuizCollection') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUIZ_QUESTION_COLLECTION_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPQuizCollection.php" );
            exit ();
        } else {
            $arr = array ();
			
            
			
            $result = array ();
            $jsonArr = array ();
            $sql = null;
			
			if($_GET ['crud'] == 'I' or $_GET ['crud'] == 'U' or $_GET ['crud'] == 'D'){
                try {
					header('Content-Type: text/html; charset=utf-8');
                
					$sql = new Mysql ( '' );
					$sql->setMysql ();
					
					foreach($_GET as $id_contr => $value) {
						$arr = array(
							'ID_CONTR' => $id_contr,
							'VL_CONTR' => $value,
							'flag' => 'U'
						);
						$result = $sql->changeQuizCollection($arr);
					}
					
					
                
					for($i = 0; $i <= count ( $result ); $i ++) {
						$jsonArr [$i] = json_encode ( $result );
					}
                
					if (substr ( $jsonArr [0], 14, 8 ) == 'ull,"pDS') {
						$_SESSION ['msg'] = '';
					} else {
						$_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
					}
				} catch ( Exception $e ) {
					echo 'Exce&ccedil;&atilde;o capturada actQuizCollection: ', $e->getMessage (), "\n";
					$result = null;
					$jsonArr = null;
				}
            
				$sql->dbDisconnect ();
				$sql = null;
            }
			
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ('', 'spTRP_QUIZ_QUESTION_COLLECTION_get' );
            
            header ( "Location: /Therappy/mainTRPQuizCollection.php" );
            exit ();
        }		
		
	// ## USER ADMINISTRATION - 501 ###
    } else if ($_POST ['action'] == 'actUser' or $_GET ['action'] == 'actUser') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ( $_POST ['search'], 'spTRP_USER_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: /Therappy/mainTRPUser.php" );
            exit ();
        } else {
            $arr = array ();            
          
            $operation = $_GET ['crud'];
            
            if ($_GET ['crud'] == 'U') {
                $arr = array (
                'ID_USER' => $_GET ['ID_USER'],
                'CD_LOGIN' => $_GET ['CD_LOGIN'],
                'DS_USER' => $_GET ['DS_USER'],
                'DT_EXPIR' => $_GET ['DT_EXPIR'],
				'ID_INSTI' => $_GET ['ID_INSTI'],				
                'NM_USER' => $_GET ['NM_USER'],				
                'flag' => 'U'.$idUsuario
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
                'ID_USER' => '',
                'CD_LOGIN' => $_GET ['CD_LOGIN'],
                'DS_USER' => $_GET ['DS_USER'],
                'DT_EXPIR' => $_GET ['DT_EXPIR'],
				'ID_INSTI' => $_GET ['ID_INSTI'],				
                'NM_USER' => $_GET ['NM_USER'],
                'flag' => 'I'.$idUsuario
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
                'ID_USER' => $_GET ['ID_USER'],
                'CD_LOGIN' => $_GET ['CD_LOGIN'],
                'DS_USER' => $_GET ['DS_USER'],
                'DT_EXPIR' => $_GET ['DT_EXPIR'],
				'ID_INSTI' => $_GET ['ID_INSTI'],				
                'NM_USER' => $_GET ['NM_USER'],
                'flag' => 'D'.$idUsuario
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
            
            try {
                $sql = new Mysql ( '' );
                $sql->setMysql ();
                $result = $sql->changeUser ( $arr );
                
                for($i = 0; $i <= count ( $result ); $i ++) {
                    $jsonArr [$i] = json_encode ( $result );
                }
                
                if (substr ( $jsonArr [0], 14, 8 ) == '","pDS_M') {
                    $_SESSION ['msg'] = '';
                } else {
                    $_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
                }
                
                $idUserFeatureAccess = $_GET["ID_USER_FEATURE_ACCESS"];
                
                $idUser = $_GET ['ID_USER'];
               
				if ($operation == 'U') {
                    //atualizar permissoes
                    //ID_USER_FEATURE_ACCESS
               
                    $idUserFeatureAccess = "{$idUserFeatureAccess}";                 
                    $sql->executeProc("spTRP_MENU_USER_chg", array($idUser, $idUserFeatureAccess));              
                } else if ($operation == 'I') {
					$idUser= $result["pID_USER_NEW"];
                    $idUserFeatureAccess = "{$idUserFeatureAccess}";                 
                    $sql->executeProc("spTRP_MENU_USER_chg", array($idUser, $idUserFeatureAccess));
                }	
            } catch ( Exception $e ) {
                echo 'Excecao capturada actUser: ', $e->getMessage (), "\n";
                $result = null;
                $jsonArr = null;
            }
            
            $sql->dbDisconnect ();
            $sql = null;
            
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ( $_POST ['search'], 'spTRP_USER_get' );
            
            header ( "Location: /Therappy/mainTRPUser.php" );
            exit ();
        }

	// ## FEATURE - 502 ###
    } else if ($_POST ['action'] == 'actFeature' or $_GET ['action'] == 'actFeature') {
        if ($_POST ['find'] == 'F') {
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ( $_POST ['search'], 'spTRP_FEATURE_get' );
            $_SESSION ['msg'] = '';
            header ( "Location: ../mainTRPMenu.php" );
            exit ();
        } else {
            $arr = array ();
           
            if ($_GET ['crud'] == 'U') {
                $arr = array (
                'ID_MENU' => $_GET ['ID_MENU'],
                'NM_MENU' => $_GET ['NM_MENU'],
                'DS_URL' => $_GET ['DS_URL'],
                'ID_MENU_PAREN' => $_GET ['ID_MENU_PAREN'],
                'ID_ORDER' => $_GET ['ID_ORDER'],
                'NM_ALIAS' => $_GET ['NM_ALIAS'],
                'ID_MENU_LOG' => $_GET ['ID_MENU_LOG'],				
				'NR_GRID_RECOR' => $_GET ['NR_GRID_RECOR'],
                'flag' => 'U',
				'ID_USER' => $idUsuario
                );
            } else if ($_GET ['crud'] == 'I') {
                $arr = array (
                'ID_MENU' => '',
                'NM_MENU' => $_GET ['NM_MENU'],
                'DS_URL' => $_GET ['DS_URL'],
                'ID_MENU_PAREN' => $_GET ['ID_MENU_PAREN'],
                'ID_ORDER' => $_GET ['ID_ORDER'],
                'NM_ALIAS' => $_GET ['NM_ALIAS'],
                'ID_MENU_LOG' => $_GET ['ID_MENU_LOG'],
				'NR_GRID_RECOR' => $_GET ['NR_GRID_RECOR'],				
                'flag' => 'I',
				'ID_USER' => $idUsuario				
                );
            } else if ($_GET ['crud'] == 'D') {
                $arr = array (
                'ID_MENU' => $_GET ['ID_MENU'],
                'NM_MENU' => '',
                'DS_URL' => '',
                'ID_MENU_PAREN' => '',
                'ID_ORDER' => '',
                'NM_ALIAS' => '',	
                'ID_MENU_LOG' => '',
				'NR_GRID_RECOR' => '',				
                'flag' => 'D',
				'ID_USER' => $idUsuario				
                );
            }
            
            $result = array ();
            $jsonArr = array ();
            $sql = null;
            
            try {
                $sql = new Mysql ( '' );
                $sql->setMysql ();
                $result = $sql->changeFeature ( $arr );
                
                for($i = 0; $i <= count ( $result ); $i ++) {
                    $jsonArr [$i] = json_encode ( $result );
                }
                
                if (substr ( $jsonArr [0], 14, 8 ) == '","pDS_M') {
                    $_SESSION ['msg'] = '';
                } else {
                    $_SESSION ['msg'] = substr ( $jsonArr [0], 14, 8 ) . ' - ' . substr ( $jsonArr [0], strpos ( $jsonArr [0], 'pDS_MESSA' ) + 12, strpos ( $jsonArr [0], 'pTP_MESSA' ) - 40 );
                }
            } catch ( Exception $e ) {
                echo 'Excecao capturada actUser: ', $e->getMessage (), "\n";
                $result = null;
                $jsonArr = null;
            }
            
            $sql->dbDisconnect ();
            $sql = null;
            
            $_POST ['action'] = null;
            $_SESSION ['arr'] = $model->loadProc ( $_POST ['search'], 'spTRP_FEATURE_get' );
            
            header ( "Location: ../mainTRPMenu.php" );
            exit ();
        }		
    }	
}
unset ( $controller );
?>