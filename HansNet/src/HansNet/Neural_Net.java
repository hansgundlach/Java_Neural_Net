package HansNet;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;


//Neural Net Regression Analysis Using Gradient Descent
public class Neural_Net {
	
   //yhat predicted output
	private double[][] yHat;
	private double[][] input;
	private double[][] output;
   //amount to adjust weight of matrix 1 for layer 1 
	private double[][]  dJdW1;
   //amount to adjust weight of  matrix 2 for layer 2
	private double[][]  dJdW2;
   //size of input(feature)  vector
	private  int input_Size;
   //number of hidden layers
	private  int hidden_Layer;
	private  int output_Size;
   //matrix of weights for hidden layer 1
	private double[][] W1 ;
   //matrix of weights for hidden layer 2 
	private double[][] W2 ;
   //z3 a2 and z2 are matrices formed by multiplying inputs from previous layer by a weight matrix
	private double[][] z3 ;
	private double[][] a2 ; 
	private double[][] z2 ;
	
	
	
	
	public static void main(String args[]) throws FileNotFoundException{
   // example input and output data
		double inputData[][] = new double[][]{{.1,.1},{.2,.2},{10,10},{20,20}};
		 double outputData[][] = new double[][]{{10},{10},{99},{105}};
		 //change this to your file location
		 File neuralFile = new File("C://Users//hansg17//Documents//GitHub//Java_Neural_Net//HansNet//src//HansNet//neural.txt");
		//readInput()
			Neural_Net  firstnet =  new Neural_Net(); 
			firstnet.train(neuralFile,2,1);
						firstnet.prediction(inputData);
	}
	//neural net class 
	public Neural_Net(){
		}
//second constructor for files
	
//trains based on input matrix X and output matrix Y 
public void train(double[][] X ,double[][] y){
	 this.input = X;
	 this.output = y;
	 //layer settings
	 this.input_Size = X[0].length;
	 this.hidden_Layer = X.length;
	 this.output_Size = y[0].length; 
	 
	 this.W1 = Mat.randmat(input_Size,hidden_Layer);
	this.W2 = Mat.randmat(hidden_Layer,output_Size);
	lernen();
	
}


//train trains neural network based on a file f with input and output matrices
//the input matrix has dim 1 columns the output matrix has dim 2 column s
public void train(File f, int dim1,int dim2) throws FileNotFoundException{
	//File newinputFile = new File("C:/Users/hansg17/workspace/newJAVart/src/newJAVart/Neural_NetInput.txt");
	
	Scanner sc = new Scanner(new FileReader(f));
	
	//Finds the size of the matrix
	 int rows = 0;
		while(sc.hasNextLine()){
		rows++;
		//System.out.println(rows);
		sc.nextLine();
		
		
	}
	sc.close();
	
	int colums = dim1+dim2;
	
	
		//scanner reads file with input and output matrices
Scanner ss = new Scanner(new FileReader(f));
	double[][] firstinput = new double[rows][colums];
	
	
	int i = 0;
	while(ss.hasNextLine()){
		
			
			 String stuff = ss.nextLine();
			 Scanner  fig = new Scanner(stuff);
			 
			 int j = 0;
			 while(fig.hasNextDouble()){
				 
				 firstinput[i][j] = fig.nextDouble();
				 j = j+1;
				 
			 }
			 i = i+1;
	}	 
		
		
	//training and sorting part 
	double[][] Start = new double[dim1][rows];
	double[][] Startout = new double[dim2][rows];
	//remember firstinput is different
	double[][] trans = transpose(firstinput);
	for(int k=0;k<dim1;k++){
		Start[k] = trans[k];
	}
	System.out.println(Arrays.deepToString(Startout));
	for(int jj=0;jj<1;jj++){
		//you may need to adjust this by 1
		System.out.println(Arrays.toString(trans[dim1+jj]));
		Startout[jj] = trans[dim1+jj];
	}
	System.out.println(Arrays.deepToString(Startout));
	train(transpose(Start),transpose(Startout));
	
}
	// lernen() combines all the methods to do full gradient descent
// and adjust weight matrices
	public void lernen(){
		scaling(input);
		scaling(output);
		//forms new weights
      //iterate until weights converge (ie approximatly 1000 times) 
		int x =0;
		//insert iteration condition base on your preference
		while(x<1000){
		forward();
		costFunction();
		costFunctionPrime();
		//subtract djdw from weight matrix 
		
		W1 = Mat.matrixSub(W1,multScal(dJdW1,1));
		W2 = Mat.matrixSub(W2,multScal(dJdW2,1));
		x++;
		}
		forward();
		costFunction();
		
		
	  }
//foward propagtes an input through the neural net
public double[][] forward(){
		//propagates inputs through network
		//z_2 = xW
	z2 = Mat.matrixMult(input,W1);
	 //a2 = f(z_3)
	 a2 = Mat.matrixSigmoid(z2);
	 //z3 = a_2 * W_2
	 z3 = Mat.matrixMult(a2,W2);
	 //this is somehow wrong
	 //yHat is supposed to be 3 by 1 matrix 
	  this.yHat = Mat.matrixSigmoid(z3);
	 
		//yHat is predicted value 
		return yHat;
		
	}


 //this is just like forrward just it does not modify network so it is for testing
 public double[][] prediction(double[][] prein){
	 //this is repetitive how can we change this
	double[][] z1 = Mat.matrixMult(prein,W1);
   //apply sigmoid activation function
	z2 = Mat.matrixSigmoid(z1);
   //generate output matrix for next layer
	 z3 = Mat.matrixMult(z2,W2);
	 double[][] prediction= Mat.matrixSigmoid(z3); 
	 System.out.println(Arrays.deepToString(prediction));
		return prediction;
	
	 
 }
	
	
	//this scales  the vectors so their can be proper comparison
 // for neural network training
	public static void scaling(double[][] prescal){
		//find maximum  value in array and divide all array entries by maxalue
		double maxvalue = 0;
		
		for(int i=0;i<prescal.length;i++){
			for(int j = 0;j<prescal[0].length;j++){
			if(prescal[i][j]>maxvalue){
				maxvalue = prescal[i][j];
			}
		}
			
		}
		for(int i=0;i<prescal.length;i++){
			for(int j = 0;j<prescal[0].length;j++){
			prescal[i][j] = prescal[i][j]/maxvalue;
			}
		}
		}
	
	//this uses the error to adjust the weight matrix using gradient descent
	public void costFunctionPrime(){
		//multiply all the derivative together in a chain rule for backpropagatin 
	double[][]  delta3 = Mat.handamard(multScal((Mat.matrixSub(output,yHat)),-1),Mat.matrixSigmoidPrime(z3));
	//adjust all weights at once
 dJdW2 = Mat.matrixMult(transpose(a2), delta3);
	
	double[][] delta2 = Mat.handamard(Mat.matrixMult(delta3 , transpose(W2)),Mat.matrixSigmoidPrime(z2));
	dJdW1 = Mat.matrixMult(transpose(input), delta2);
	}
	//this cost function measures the accuracy of our prediction ie yHat
   //to Output
	public double  costFunction(){
		
		double cost = 0;
	for(int i=0;i<yHat.length;i++){
			cost = .5* (output[i][0] - yHat[i][0]);
			
		}
		
		System.out.println(cost);
		return cost;
	}
	

	
	//transposes a give matrix untrans
	public static double[][] transpose(double[][] untrans){
		double[][] temp = new double[untrans[0].length][untrans.length];
		for(int i = 0;i< untrans.length;i++){
			for(int j= 0;j<untrans[0].length;j++){
				temp[j][i] = untrans[i][j];
		}
		}
		untrans = temp;
		return untrans;
			}
	//multiplies a matrix by a scalar
	public  static double[][] multScal(double[][] inin, double scalar){
		for(int i = 0;i<inin.length;i++){
			for(int j = 0;j<inin[0].length;j++){
				inin[i][j] = inin[i][j]*scalar;
			}
		}
		return inin;
	}

	
	
}