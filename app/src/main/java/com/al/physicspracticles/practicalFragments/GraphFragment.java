package com.al.physicspracticles.practicalFragments;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.al.physicspracticles.R;
import com.al.physicspracticles.databinding.FragmentGraphBinding;
import com.al.physicspracticles.databinding.FragmentPdfBinding;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.Size;
import com.androidplot.ui.TableOrder;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.chaquo.python.PyObject;
import com.google.android.material.snackbar.Snackbar;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;


public class GraphFragment extends Fragment {

    private FragmentGraphBinding binding;
    private static ArrayList<Double> x_axis;
    private static ArrayList<Double> y_axis;
    private static ArrayList<Double> x_axis_new;
    private static ArrayList<Double> y_axis_new;
    private static ArrayList<Double> cover;
    private Number[] domainLabels;
    private Number[] rangeLabels;
    private Number[] rangeLabels_original;
    private Number[] series2Numbers ;
    private Number[] XNumbers;
    private Number[] YNumbers;
    private Double step=5.0;
    private Double x_step=0.0;
    private Double y_step=0.0;
    private static int type;
    private int graph_count=0;
    private XYSeries series1;
    private XYSeries seriesOriginal;
    private  XYSeries seriesCover;
    private String FUNCTION;
    private static int colors[]={Color.RED,Color.BLUE};
    public GraphFragment(int type) {this.type=type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentGraphBinding.inflate(inflater,container,false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.plotGraph.clear();
                    binding.plotGraph.setVisibility(View.INVISIBLE);


                graph_count=0;
            }
        });
        binding.btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type==0){
                    binding.plotGraph.clear();
                    binding.plotGraph.setVisibility(View.INVISIBLE);
                    x_axis=new ArrayList<Double>();
                    y_axis=new ArrayList<Double>();
                    if(getDataX(binding.txtX) && getDataY(binding.txtY)) {
                        if(x_axis.size()!=y_axis.size()){
                            Snackbar.make( binding.getRoot(),"X,Y Value counts are not equal", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else if(x_axis.size()<6){
                            Snackbar.make( binding.getRoot(),"There should be more than 6 coordinates", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else {

                            String function = findFunction(3);

                            if (!function.equals("None")){
                                plot2(function, x_axis_new.get(0), x_axis_new.get(x_axis_new.size() - 1));

                            binding.plotGraph.setVisibility(View.VISIBLE);
                        }else{

                            Snackbar.make(binding.getRoot(), "If x/y or y/x <0.01 , use another scale for x and y (ex: s-->ms)", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        }


                    }else{
                        Snackbar.make( binding.getRoot(),"Invalid coordinates", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }else if (type==1){
                    binding.plotGraph.clear();
                    binding.plotGraph.setVisibility(View.INVISIBLE);
                    x_axis=new ArrayList<Double>();
                    y_axis=new ArrayList<Double>();
                    if(getDataX(binding.txtX) && getDataY(binding.txtY)) {
                        if(x_axis.size()!=y_axis.size()){
                            Snackbar.make( binding.getRoot(),"X,Y Value counts are not equal", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else if(x_axis.size()<6){
                            Snackbar.make( binding.getRoot(),"There should be more than 6 coordinates", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else{
                            normalPlot(false);
                            binding.plotGraph.setVisibility(View.VISIBLE);
                        }


                    }else{
                        Toast.makeText(getContext(), "Invalid coordinates", Toast.LENGTH_SHORT).show();
                    }
                }else if(type==2){
                    x_axis=new ArrayList<Double>();
                    y_axis=new ArrayList<Double>();
                    if(getDataX(binding.txtX) && getDataY(binding.txtY)) {
                        if(x_axis.size()!=y_axis.size()){
                            Snackbar.make( binding.getRoot(),"X,Y Value counts are not equal", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else if(x_axis.size()<6){
                            Snackbar.make( binding.getRoot(),"There should be more than 6 coordinates", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else{
                            if(graph_count<2){
                                normalPlot(true);
                                binding.plotGraph.setVisibility(View.INVISIBLE);
                                binding.plotGraph.setVisibility(View.VISIBLE);
                            }else{
                                Toast.makeText(getContext(),"Only 2 graphs can be plotted at same time",Toast.LENGTH_SHORT).show();
                            }
                        }


                    }else{
                        Toast.makeText(getContext(), "Invalid coordinates", Toast.LENGTH_SHORT).show();
                    }
                }




            }



        });


        return binding.getRoot();
    }

    private boolean getDataX(EditText view){
        try{
            String data=view.getText().toString();
            String[] list_data=data.split(" ");
            for(int i=0;i<list_data.length;i++){
                if(!list_data[i].equals("")){
                    x_axis.add(Double.parseDouble(list_data[i]));
                }
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }
    private boolean getDataY(EditText view){
        try{
            String data=view.getText().toString();
            String[] list_data=data.split(" ");
            for(int i=0;i<list_data.length;i++){
                if(!list_data[i].equals("")){
                    y_axis.add(Double.parseDouble(list_data[i]));
                }
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }
    private String findFunction(int points){

        Double xsum=0.0,ysum=0.0,xysum=0.0,x2sum=0.0;
        Double m,c;

        if(x_axis.get(0)/y_axis.get(0)<0.01 ||y_axis.get(0)/x_axis.get(0)<0.01){
            return "None";
        }
        int n=x_axis.size();
        for(int i=0;i<n;i++){
            xsum=(x_axis.get(i))+xsum;
            ysum=(y_axis.get(i))+ysum;
            xysum=(x_axis.get(i))*(y_axis.get(i))+xysum;
            x2sum=(x_axis.get(i))*(x_axis.get(i))+x2sum;

        }


            m= (n * xysum - xsum * ysum) / (n * x2sum - xsum * xsum);

            c = (x2sum * ysum - xsum * xysum) / (x2sum * n - xsum * xsum);



        String function=null;
        c=round(c,2);
        m=round(m,2);
        if(c>0){
            function="y="+m+"*x+"+c;
        }else if (c==0){
            function="y="+m+"*x";
        }else{
            function="y="+m+"*x-"+(-1*c);
        }





        x_axis_new=x_axis;
        y_axis_new=new ArrayList<Double>();

        //line y
        for (int i=0;i<x_axis_new.size();i++){
            y_axis_new.add(m*x_axis_new.get(i)+c);
        }


        domainLabels=new Number[x_axis_new.size()+2];
        rangeLabels=new Number[x_axis_new.size()+2];
        Double x_gap=x_axis_new.get(1)-x_axis_new.get(0);

        domainLabels[0]=x_axis_new.get(0)-x_gap;
        domainLabels[domainLabels.length-1]=x_axis_new.get(x_axis_new.size()-1)+x_gap;

        rangeLabels[0]=m*(Double) domainLabels[0]+c;
        rangeLabels[rangeLabels.length-1]=m*(Double) domainLabels[domainLabels.length-1]+c;




        for(int j=1;j<domainLabels.length-1;j++) {
            domainLabels[j]=round(x_axis_new.get(j-1),points);
            rangeLabels[j]=round(y_axis_new.get(j-1),points);
        }

        //experimental y
        rangeLabels_original=new Number[y_axis.size()+2];
        rangeLabels_original[0]=null;
        rangeLabels_original[rangeLabels_original.length-1]=null;
        for(int j=1;j<rangeLabels_original.length-1;j++) {
            rangeLabels_original[j]=round(y_axis.get(j-1),points);
        }

        x_step=round((x_axis_new.get(x_axis_new.size()-1)-x_axis_new.get(0))/10,0);
        y_step=(y_axis_new.get(y_axis_new.size()-1)-y_axis_new.get(0))/10;
        if(y_step<=0.1){
            y_step=0.1;
        }else if(0.1<y_step && y_step<=0.5){
            y_step=0.5;
        }else{
            y_step=round((y_axis_new.get(y_axis_new.size()-1)-y_axis_new.get(0))/10,0);
        }

        cover=new ArrayList<Double>();
        Double start=round((Double) rangeLabels[0],0)-y_step;
        Double end=round((Double) rangeLabels[rangeLabels.length-1],0)+y_step;
        cover.add(start);
        for(int j=1;j<domainLabels.length-1;j++) {
            cover.add(0.0);
        }
        cover.add(end);

        XNumbers=new Number[domainLabels.length];
        for(int j=0;j<XNumbers.length;j++){
            XNumbers[j]=0;
        }

        return function;


    }
    private void normalPlot(Boolean plot2){
        // create a couple arrays of y-values to plot:

        if(!plot2){
            binding.plotGraph.clear();
            graph_count=0;
        }


        cover=new ArrayList<Double>();
        Double a[]=new Double[y_axis.size()];
        y_axis.toArray(a);
        Double mid=round(min(a),0)-2.0;


        for(int j=0;j<x_axis.size();j++) {
            cover.add(mid);
        }


        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
         series1= new SimpleXYSeries(
                y_axis, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, String.valueOf(graph_count+1));


        XYSeries seriesCover = new SimpleXYSeries(
                cover,SimpleXYSeries.ArrayFormat.Y_VALS_ONLY ,"");



        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(colors[graph_count], Color.GREEN, null, null);


      LineAndPointFormatter coverFormat =
                new LineAndPointFormatter(null,null  , null, null);



        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Uniform));

        // add a new series' to the xyplot:

        binding.plotGraph.addSeries(series1, series1Format);
        binding.plotGraph.addSeries(seriesCover,coverFormat);

        binding.plotGraph.setDomainStep(StepMode.INCREMENT_BY_VAL,1);

        binding.plotGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(x_axis.get(i));
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        graph_count++;
    }


    private void plot2(String input,Double start,Double end){
        // create a couple arrays of y-values to plot:



        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        series1 = new SimpleXYSeries(
                Arrays.asList(rangeLabels), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, input);

        XYSeries seriesX = new SimpleXYSeries(
                Arrays.asList(XNumbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "y=0");

        seriesOriginal = new SimpleXYSeries(
                Arrays.asList(rangeLabels_original),SimpleXYSeries.ArrayFormat.Y_VALS_ONLY ,"");
        seriesCover = new SimpleXYSeries(
                cover,SimpleXYSeries.ArrayFormat.Y_VALS_ONLY ,"");



        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, null, null, null);



        LineAndPointFormatter XFormat =
                new LineAndPointFormatter(Color.BLACK,null  , null, null);
        LineAndPointFormatter OriginalFormat =
                new LineAndPointFormatter(null,Color.BLUE  , null, null);
        LineAndPointFormatter coverFormat =
                new LineAndPointFormatter(null,null  , null, null);



        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Uniform));

        // add a new series' to the xyplot:

        binding.plotGraph.addSeries(series1, series1Format);
        binding.plotGraph.addSeries(seriesX,XFormat);
        binding.plotGraph.addSeries(seriesOriginal,OriginalFormat);
        binding.plotGraph.addSeries(seriesCover,coverFormat);

        binding.plotGraph.setDomainStep(StepMode.INCREMENT_BY_VAL,1);
        binding.plotGraph.setRangeStep(StepMode.INCREMENT_BY_VAL,y_step);

        binding.plotGraph.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
        binding.plotGraph.setTitle(input);
        binding.plotGraph.getLegend().setVisible(true);
    }
    private Double max(Double[] set){
        Double m=set[0];
        int i=1;
        while(i<set.length){
            if (m>set[i]){
                m=set[i];
            }
            i++;
        }
        return m;
    }
    private Double min(Double[] set){
        Double m=set[0];
        int i=1;
        while(i<set.length){
            if (m>set[i]){
                m=set[i];
            }
            i++;
        }
        return m;
    }


    private Double round(Double v,int points){
        char[] value=String.valueOf(v).toCharArray();
        int index_floating=0;
        for (int i=0;i<value.length;i++){
            if(value[i]=='.'){
                index_floating=i;
                break;
            }
        }

        if(value.length<index_floating+1+points){
            return Double.valueOf(String.valueOf(value));
        }
        char[] res=new char[index_floating+1+points];
        for (int i=0;i<res.length;i++){
            res[i]=value[i];
        }
        return Double.valueOf(String.valueOf(res));
    }
}