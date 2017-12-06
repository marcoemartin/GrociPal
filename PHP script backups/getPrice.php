<?php
	$con = mysql_connect('localhost','root');
	$query = $_POST['Query'];
	//$query = "SELECT price from ExampleStore WHERE productnum = 1212 or productnum = 1313";
	
	if(!$con){
		die('could not connect: '.mysql_error());
	}
	
	mysql_select_db('AllItems',$con);
	$result = mysql_query($query);

	while($row=mysql_fetch_assoc($result)){
		//print($row);
		$output[]=$row;
	}

	//Returns JSON encoded result
	//print(json_encode($output));
	
	$total = 0;
	$size = sizeof($output);
	//Testing other outputs
	for ($i = 0; $i < $size;$i++){
		foreach ($output[$i] as $item){
			$total = $total + $item;
		}
	}
	print(json_encode($total));	
	
	
	
	mysql_close($con); ?>