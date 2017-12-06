<?php
	//Gets $num from android application
	$con=mysql_connect('localhost','root');
	//$num = 	$_POST['ProductNumber'];

	if(!$con){
	die('count not connect: ' .mysql_error());
		}
		
		
	mysql_select_db('AllItems',$con);
	//Queries Database
	$result = mysql_query("SELECT * FROM ExampleStore");
	

	
	while($row=mysql_fetch_assoc($result)){
		$output[]=$row;
	}

	//Returns JSON encoded result
	print(json_encode($output));

	mysql_close($con);


?>