package com.ayubo.life.ayubolife.reports.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ChartDatum implements Serializable
        //graph_visible
{
                @SerializedName("parameter_id")
                @Expose
                private String parameterId;

                @SerializedName("graph_visible")
                @Expose
                private boolean is_graph_visible;


                @SerializedName("parameter_name")
                @Expose
                private String parameterName;

                @SerializedName("uom")
                @Expose
                private String uom;

                @SerializedName("x_axis")
                @Expose
                private List<String> xAxis = null;

                @SerializedName("y_axis")
                @Expose
                private List<String> yAxis = null;
                private final static long serialVersionUID = 7801885980070627962L;


        public boolean isIs_graph_visible() {
                return is_graph_visible;
        }

        public void setIs_graph_visible(boolean is_graph_visible) {
                this.is_graph_visible = is_graph_visible;
        }

        public String getParameterId() {
                        return parameterId;
                }

                public void setParameterId(String parameterId) {
                        this.parameterId = parameterId;
                }

                public String getParameterName() {
                        return parameterName;
                }

                public void setParameterName(String parameterName) {
                        this.parameterName = parameterName;
                }

                public String getUom() {
                        return uom;
                }

                public void setUom(String uom) {
                        this.uom = uom;
                }

                public List<String> getXAxis() {
                        return xAxis;
                }

                public void setXAxis(List<String> xAxis) {
                        this.xAxis = xAxis;
                }

                public List<String> getYAxis() {
                        return yAxis;
                }

                public void setYAxis(List<String> yAxis) {
                        this.yAxis = yAxis;
                }

        }