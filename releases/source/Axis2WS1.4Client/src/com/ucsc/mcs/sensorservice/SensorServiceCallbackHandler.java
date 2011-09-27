
/**
 * SensorServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.ucsc.mcs.sensorservice;

    /**
     *  SensorServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class SensorServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public SensorServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public SensorServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for GetMessage method
            * override this method for handling normal response from GetMessage operation
            */
           public void receiveResultGetMessage(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.Out result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMessage operation
           */
            public void receiveErrorGetMessage(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for EmailData method
            * override this method for handling normal response from EmailData operation
            */
           public void receiveResultEmailData(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.EmailDataResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from EmailData operation
           */
            public void receiveErrorEmailData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for UploadData method
            * override this method for handling normal response from UploadData operation
            */
           public void receiveResultUploadData(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.UploadDataResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from UploadData operation
           */
            public void receiveErrorUploadData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for AddJob method
            * override this method for handling normal response from AddJob operation
            */
           public void receiveResultAddJob(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.AddJobResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from AddJob operation
           */
            public void receiveErrorAddJob(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for Subscribe method
            * override this method for handling normal response from Subscribe operation
            */
           public void receiveResultSubscribe(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.SubscribeResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from Subscribe operation
           */
            public void receiveErrorSubscribe(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for Login method
            * override this method for handling normal response from Login operation
            */
           public void receiveResultLogin(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.LoginResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from Login operation
           */
            public void receiveErrorLogin(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetReferenceData method
            * override this method for handling normal response from GetReferenceData operation
            */
           public void receiveResultGetReferenceData(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.GetReferenceDataResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetReferenceData operation
           */
            public void receiveErrorGetReferenceData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for CalculateRectArea method
            * override this method for handling normal response from CalculateRectArea operation
            */
           public void receiveResultCalculateRectArea(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.Area result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from CalculateRectArea operation
           */
            public void receiveErrorCalculateRectArea(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ViewData method
            * override this method for handling normal response from ViewData operation
            */
           public void receiveResultViewData(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.ViewDataResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ViewData operation
           */
            public void receiveErrorViewData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetJobs method
            * override this method for handling normal response from GetJobs operation
            */
           public void receiveResultGetJobs(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.GetJobsResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetJobs operation
           */
            public void receiveErrorGetJobs(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ViewJob method
            * override this method for handling normal response from ViewJob operation
            */
           public void receiveResultViewJob(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.ViewJobResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ViewJob operation
           */
            public void receiveErrorViewJob(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PasswordRecover method
            * override this method for handling normal response from PasswordRecover operation
            */
           public void receiveResultPasswordRecover(
                    com.ucsc.mcs.sensorservice.SensorServiceStub.PasswordRecoverResponseType result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PasswordRecover operation
           */
            public void receiveErrorPasswordRecover(java.lang.Exception e) {
            }
                


    }
    