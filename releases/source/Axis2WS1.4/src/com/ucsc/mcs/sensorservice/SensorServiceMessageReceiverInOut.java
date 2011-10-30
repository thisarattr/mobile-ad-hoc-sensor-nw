
/**
 * SensorServiceMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
        package com.ucsc.mcs.sensorservice;

        /**
        *  SensorServiceMessageReceiverInOut message receiver
        */

        public class SensorServiceMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        SensorServiceSkeleton skel = (SensorServiceSkeleton)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)){

        

            if("GetMessage".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.Out out1 = null;
	                        com.ucsc.mcs.sensorservice.Input wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.Input)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.Input.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               out1 =
                                                   
                                                   
                                                         skel.GetMessage(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), out1, false);
                                    } else 

            if("EmailData".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.EmailDataResponseType emailDataResponseType3 = null;
	                        com.ucsc.mcs.sensorservice.EmailDataRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.EmailDataRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.EmailDataRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               emailDataResponseType3 =
                                                   
                                                   
                                                         skel.EmailData(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), emailDataResponseType3, false);
                                    } else 

            if("UploadData".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.UploadDataResponseType uploadDataResponseType5 = null;
	                        com.ucsc.mcs.sensorservice.UploadDataReuestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.UploadDataReuestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.UploadDataReuestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               uploadDataResponseType5 =
                                                   
                                                   
                                                         skel.UploadData(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), uploadDataResponseType5, false);
                                    } else 

            if("AddJob".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.AddJobResponseType addJobResponseType7 = null;
	                        com.ucsc.mcs.sensorservice.AddJobRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.AddJobRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.AddJobRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addJobResponseType7 =
                                                   
                                                   
                                                         skel.AddJob(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addJobResponseType7, false);
                                    } else 

            if("Subscribe".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.SubscribeResponseType subscribeResponseType9 = null;
	                        com.ucsc.mcs.sensorservice.SubscribeRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.SubscribeRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.SubscribeRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               subscribeResponseType9 =
                                                   
                                                   
                                                         skel.Subscribe(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), subscribeResponseType9, false);
                                    } else 

            if("Login".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.LoginResponseType loginResponseType11 = null;
	                        com.ucsc.mcs.sensorservice.LoginRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.LoginRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.LoginRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               loginResponseType11 =
                                                   
                                                   
                                                         skel.Login(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), loginResponseType11, false);
                                    } else 

            if("EditUser".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.EditUserResponseType editUserResponseType13 = null;
	                        com.ucsc.mcs.sensorservice.EditUserRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.EditUserRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.EditUserRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               editUserResponseType13 =
                                                   
                                                   
                                                         skel.EditUser(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), editUserResponseType13, false);
                                    } else 

            if("EditJob".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.EditJobResponseType editJobResponseType15 = null;
	                        com.ucsc.mcs.sensorservice.EditJobRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.EditJobRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.EditJobRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               editJobResponseType15 =
                                                   
                                                   
                                                         skel.EditJob(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), editJobResponseType15, false);
                                    } else 

            if("GetReferenceData".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.GetReferenceDataResponseType getReferenceDataResponseType17 = null;
	                        com.ucsc.mcs.sensorservice.GetReferenceDataRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.GetReferenceDataRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.GetReferenceDataRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getReferenceDataResponseType17 =
                                                   
                                                   
                                                         skel.GetReferenceData(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getReferenceDataResponseType17, false);
                                    } else 

            if("CalculateRectArea".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.Area area19 = null;
	                        com.ucsc.mcs.sensorservice.Parameters wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.Parameters)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.Parameters.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               area19 =
                                                   
                                                   
                                                         skel.CalculateRectArea(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), area19, false);
                                    } else 

            if("ViewData".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.ViewDataResponseType viewDataResponseType21 = null;
	                        com.ucsc.mcs.sensorservice.ViewDataRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.ViewDataRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.ViewDataRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               viewDataResponseType21 =
                                                   
                                                   
                                                         skel.ViewData(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), viewDataResponseType21, false);
                                    } else 

            if("GetJobs".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.GetJobsResponseType getJobsResponseType23 = null;
	                        com.ucsc.mcs.sensorservice.GetJobsRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.GetJobsRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.GetJobsRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getJobsResponseType23 =
                                                   
                                                   
                                                         skel.GetJobs(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getJobsResponseType23, false);
                                    } else 

            if("ViewJob".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.ViewJobResponseType viewJobResponseType25 = null;
	                        com.ucsc.mcs.sensorservice.ViewJobRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.ViewJobRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.ViewJobRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               viewJobResponseType25 =
                                                   
                                                   
                                                         skel.ViewJob(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), viewJobResponseType25, false);
                                    } else 

            if("PasswordRecover".equals(methodName)){
                
                com.ucsc.mcs.sensorservice.PasswordRecoverResponseType passwordRecoverResponseType27 = null;
	                        com.ucsc.mcs.sensorservice.PasswordRecoverRequestType wrappedParam =
                                                             (com.ucsc.mcs.sensorservice.PasswordRecoverRequestType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    com.ucsc.mcs.sensorservice.PasswordRecoverRequestType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               passwordRecoverResponseType27 =
                                                   
                                                   
                                                         skel.PasswordRecover(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), passwordRecoverResponseType27, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.Input param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.Input.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.Out param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.Out.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.EmailDataRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.EmailDataRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.EmailDataResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.EmailDataResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.UploadDataReuestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.UploadDataReuestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.UploadDataResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.UploadDataResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.AddJobRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.AddJobRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.AddJobResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.AddJobResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.SubscribeRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.SubscribeRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.SubscribeResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.SubscribeResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.LoginRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.LoginRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.LoginResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.LoginResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.EditUserRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.EditUserRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.EditUserResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.EditUserResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.EditJobRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.EditJobRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.EditJobResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.EditJobResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.GetReferenceDataRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.GetReferenceDataRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.GetReferenceDataResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.GetReferenceDataResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.Parameters param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.Parameters.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.Area param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.Area.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.ViewDataRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.ViewDataRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.ViewDataResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.ViewDataResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.GetJobsRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.GetJobsRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.GetJobsResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.GetJobsResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.ViewJobRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.ViewJobRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.ViewJobResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.ViewJobResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.PasswordRecoverRequestType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.PasswordRecoverRequestType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(com.ucsc.mcs.sensorservice.PasswordRecoverResponseType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(com.ucsc.mcs.sensorservice.PasswordRecoverResponseType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.Out param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.Out.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.Out wrapGetMessage(){
                                com.ucsc.mcs.sensorservice.Out wrappedElement = new com.ucsc.mcs.sensorservice.Out();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.EmailDataResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.EmailDataResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.EmailDataResponseType wrapEmailData(){
                                com.ucsc.mcs.sensorservice.EmailDataResponseType wrappedElement = new com.ucsc.mcs.sensorservice.EmailDataResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.UploadDataResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.UploadDataResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.UploadDataResponseType wrapUploadData(){
                                com.ucsc.mcs.sensorservice.UploadDataResponseType wrappedElement = new com.ucsc.mcs.sensorservice.UploadDataResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.AddJobResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.AddJobResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.AddJobResponseType wrapAddJob(){
                                com.ucsc.mcs.sensorservice.AddJobResponseType wrappedElement = new com.ucsc.mcs.sensorservice.AddJobResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.SubscribeResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.SubscribeResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.SubscribeResponseType wrapSubscribe(){
                                com.ucsc.mcs.sensorservice.SubscribeResponseType wrappedElement = new com.ucsc.mcs.sensorservice.SubscribeResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.LoginResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.LoginResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.LoginResponseType wrapLogin(){
                                com.ucsc.mcs.sensorservice.LoginResponseType wrappedElement = new com.ucsc.mcs.sensorservice.LoginResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.EditUserResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.EditUserResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.EditUserResponseType wrapEditUser(){
                                com.ucsc.mcs.sensorservice.EditUserResponseType wrappedElement = new com.ucsc.mcs.sensorservice.EditUserResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.EditJobResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.EditJobResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.EditJobResponseType wrapEditJob(){
                                com.ucsc.mcs.sensorservice.EditJobResponseType wrappedElement = new com.ucsc.mcs.sensorservice.EditJobResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.GetReferenceDataResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.GetReferenceDataResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.GetReferenceDataResponseType wrapGetReferenceData(){
                                com.ucsc.mcs.sensorservice.GetReferenceDataResponseType wrappedElement = new com.ucsc.mcs.sensorservice.GetReferenceDataResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.Area param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.Area.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.Area wrapCalculateRectArea(){
                                com.ucsc.mcs.sensorservice.Area wrappedElement = new com.ucsc.mcs.sensorservice.Area();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.ViewDataResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.ViewDataResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.ViewDataResponseType wrapViewData(){
                                com.ucsc.mcs.sensorservice.ViewDataResponseType wrappedElement = new com.ucsc.mcs.sensorservice.ViewDataResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.GetJobsResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.GetJobsResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.GetJobsResponseType wrapGetJobs(){
                                com.ucsc.mcs.sensorservice.GetJobsResponseType wrappedElement = new com.ucsc.mcs.sensorservice.GetJobsResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.ViewJobResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.ViewJobResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.ViewJobResponseType wrapViewJob(){
                                com.ucsc.mcs.sensorservice.ViewJobResponseType wrappedElement = new com.ucsc.mcs.sensorservice.ViewJobResponseType();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, com.ucsc.mcs.sensorservice.PasswordRecoverResponseType param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(com.ucsc.mcs.sensorservice.PasswordRecoverResponseType.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private com.ucsc.mcs.sensorservice.PasswordRecoverResponseType wrapPasswordRecover(){
                                com.ucsc.mcs.sensorservice.PasswordRecoverResponseType wrappedElement = new com.ucsc.mcs.sensorservice.PasswordRecoverResponseType();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (com.ucsc.mcs.sensorservice.Input.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.Input.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.Out.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.Out.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.EmailDataRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.EmailDataRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.EmailDataResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.EmailDataResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.UploadDataReuestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.UploadDataReuestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.UploadDataResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.UploadDataResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.AddJobRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.AddJobRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.AddJobResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.AddJobResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.SubscribeRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.SubscribeRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.SubscribeResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.SubscribeResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.LoginRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.LoginRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.LoginResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.LoginResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.EditUserRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.EditUserRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.EditUserResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.EditUserResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.EditJobRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.EditJobRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.EditJobResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.EditJobResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.GetReferenceDataRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.GetReferenceDataRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.GetReferenceDataResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.GetReferenceDataResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.Parameters.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.Parameters.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.Area.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.Area.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.ViewDataRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.ViewDataRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.ViewDataResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.ViewDataResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.GetJobsRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.GetJobsRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.GetJobsResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.GetJobsResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.ViewJobRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.ViewJobRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.ViewJobResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.ViewJobResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.PasswordRecoverRequestType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.PasswordRecoverRequestType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (com.ucsc.mcs.sensorservice.PasswordRecoverResponseType.class.equals(type)){
                
                           return com.ucsc.mcs.sensorservice.PasswordRecoverResponseType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    