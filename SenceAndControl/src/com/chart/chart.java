package com.chart; 

import java.io.File; 
import java.io.FileNotFoundException; 
import java.io.FileOutputStream; 
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.chart.ChartFactory; 
import org.jfree.chart.ChartUtilities; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator; 
import org.jfree.chart.plot.CategoryPlot; 
import org.jfree.chart.plot.PlotOrientation; 
import org.jfree.chart.renderer.category.LineAndShapeRenderer; 
import org.jfree.data.category.CategoryDataset; 
import org.jfree.data.general.DatasetUtilities; 

//JFreeChart Line Chart（折线图）   
public class chart{ 
	public static double[][]DATA;
	// 步骤1：创建CategoryDataset对象（准备数据） 
	public CategoryDataset dataset = createDataset();  
	//public CategoryDataset dataset1 = createDataset1();
	// 步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置  
	public JFreeChart freeChart = createChart(dataset); 
	//public JFreeChart freeChart1 = createChart1(dataset1); 

	//根据CategoryDataset创建JFreeChart对象
	public static JFreeChart createChart(CategoryDataset categoryDateset){

		// 创建JFreeChart对象：ChartFactory.createLineChart  
		JFreeChart jfreechart = ChartFactory.createLineChart("Time To Temp Simulation", // 标题
				"Time",         //categoryAxisLabel （category轴，横轴，X轴标签）
				"Temp",      // valueAxisLabel（value轴，纵轴，Y轴的标签）
				categoryDateset,  //Dataset  
				PlotOrientation.VERTICAL, false, // legend 
				false,          //Tooltips
				false);        //URLs

		// 使用CategoryPlot设置各种参数。  
		CategoryPlot plot = (CategoryPlot)jfreechart.getPlot();  

		// 背景色 透明度  
		plot.setBackgroundAlpha(0.5f);  

		// 前景色 透明度  
		plot.setForegroundAlpha(1.0f);  


		// 其他设置 参考 CategoryPlot类  
		LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();  
		renderer.setBaseShapesVisible(true); // series 点（即数据点）可见  
		renderer.setBaseLinesVisible(true); // series 点（即数据点）间有连线可见  
		renderer.setUseSeriesOffset(true); // 设置偏移量  
		renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());  
		renderer.setBaseItemLabelsVisible(true);  
		return jfreechart;  
	}

	public static CategoryDataset createDataset() {  
		 
		 String[] rowKeys = {"Sort"};  
	    //    String[] colKeys = {"HeapSort","ShellSort","BubbleSort","QuickSort","RadixSort","SimpleSort"};  
	        
	    //    double[][] data = {{1,8,7,9,6,4}};   
	        //System.out.println(TimeChat.btime_Heap);
		
		//String[] colKeys = {"0","1","2","3","4","5","6","7","8","9"};
		 int MAX=100;
		 double TN=30;
		 double TS=12;
		 double TP=3;
		 double TJ=1;
		 String[] colKeys=new String[MAX];
		for(int i=0;i<MAX;i++)
			colKeys[i]= String.valueOf(i);
		//String s = String.valueOf(i);
		double[][] data=new double[1][MAX];
		boolean tru=true;
		for(int i=0;i<MAX;i++)
		{
			if(TN==TS+TP)
				tru=true;

			if(tru)//?>9
			{
				data[0][i]=TN-TJ ;
				TN-=TJ;
			}
			if(TN==TS-TP)
				tru=false;
			if(!tru)//?<15
			{
				data[0][i]=TN+TJ ;
				TN+=TJ;
			}
		}

		//  double[][] data = {{TimeChat.btime_Heap,TimeChat.btime_Shell,TimeChat.btime_Bubble,TimeChat.btime_Quick,TimeChat.btime_Radix,TimeChat.btime_Simple},};   
		//System.out.println(TimeChat.btime_Heap);
		return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);  
	}
	
}

