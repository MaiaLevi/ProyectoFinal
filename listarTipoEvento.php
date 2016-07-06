<?php
$con=mysqli_connect('localhost','u939089919_daius','42038123','u939089919_bd');
if (mysqli_connect_errno()) {
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
   
$query = 'SELECT * FROM tipo';
$result = mysqli_query($con, $query);
$objetos = array();
while($row = mysqli_fetch_array($result)) 
{ 
	$IdTipo=$row['IdTipo'];
	$Nombre=$row['Nombre'];
	$objeto = array('IdTipo'=> $IdTipo, 'Nombre'=> $Nombre);	
    	$objetos[] = $objeto;
	
}
$close = mysqli_close($con) 
or die("Ha sucedido un error inesperado en la desconexion de la base de datos");
header("Content-Type: application/json");
$json_string = json_encode($objetos,JSON_UNESCAPED_UNICODE);
echo $json_string;
?>