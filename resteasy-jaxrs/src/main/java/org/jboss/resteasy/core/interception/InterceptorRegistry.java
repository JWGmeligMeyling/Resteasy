package org.jboss.resteasy.core.interception;

import org.jboss.resteasy.core.ResourceMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class InterceptorRegistry
{
   private List<Class> perResourceMethodInterceptorClasses = new ArrayList<Class>();
   private List<ResourceMethodInterceptor> resourceMethodIntercptors = new ArrayList<ResourceMethodInterceptor>();

   public List<Class> getPerResourceMethodInterceptorClasses()
   {
      return perResourceMethodInterceptorClasses;
   }

   public List<ResourceMethodInterceptor> getResourceMethodInterceptors()
   {
      return resourceMethodIntercptors;
   }

   public void registerResourceMethodInterceptor(Class clazz)
   {
      perResourceMethodInterceptorClasses.add(clazz);
   }

   public void registerResourceMethodInterceptors(Class[] classes)
   {
      for (Class clazz : classes) registerResourceMethodInterceptor(clazz);
   }

   public void registerResourceMethodInterceptor(ResourceMethodInterceptor interceptor)
   {
      resourceMethodIntercptors.add(interceptor);
   }

   public ResourceMethodInterceptor[] bind(ResourceMethod method)
   {
      List<ResourceMethodInterceptor> list = new ArrayList<ResourceMethodInterceptor>();
      for (Class<ResourceMethodInterceptor> clazz : perResourceMethodInterceptorClasses)
      {
         ResourceMethodInterceptor interceptor = null;
         try
         {
            interceptor = clazz.newInstance();
         }
         catch (InstantiationException e)
         {
            throw new RuntimeException(e);
         }
         catch (IllegalAccessException e)
         {
            throw new RuntimeException(e);
         }
         if (interceptor.accepted(method))
         {
            list.add(interceptor);
         }
      }
      for (ResourceMethodInterceptor interceptor : resourceMethodIntercptors)
      {
         if (interceptor.accepted(method))
         {
            list.add(interceptor);
         }
      }
      return list.toArray(new ResourceMethodInterceptor[0]);
   }

   protected List bindInterceptors(List<Class> classes, List singletons, Class declaring, Method method)
   {
      List list = new ArrayList();
      for (Class clazz : classes)
      {
         Object interceptor = null;
         try
         {
            interceptor = clazz.newInstance();
         }
         catch (InstantiationException e)
         {
            throw new RuntimeException(e);
         }
         catch (IllegalAccessException e)
         {
            throw new RuntimeException(e);
         }
         bindInterceptor(interceptor, declaring, method, list);
      }
      for (Object interceptor : singletons) bindInterceptor(interceptor, declaring, method, list);
      return list;
   }

   protected void bindInterceptor(Object interceptor, Class declaring, Method method, List list)
   {
      if (interceptor instanceof AcceptedByMethod)
      {
         AcceptedByMethod accepted = (AcceptedByMethod) interceptor;
         if (accepted.accept(declaring, method))
         {
            list.add(interceptor);
         }
      }
      else
      {
         list.add(interceptor);
      }
   }

   private List<Class> readerInterceptorClasses = new ArrayList<Class>();
   private List<MessageBodyReaderInterceptor> readerInterceptors = new ArrayList<MessageBodyReaderInterceptor>();

   public List<Class> getMessageBodyReaderInterceptorClasses()
   {
      return readerInterceptorClasses;
   }

   public List<MessageBodyReaderInterceptor> getMessageBodyReaderInterceptorInterceptors()
   {
      return readerInterceptors;
   }

   public void registerMessageBodyReaderInterceptor(Class clazz)
   {
      readerInterceptorClasses.add(clazz);
   }

   public void registerMessageBodyReaderInterceptors(Class[] classes)
   {
      for (Class clazz : classes) registerMessageBodyReaderInterceptor(clazz);
   }

   public void registerMessageBodyReaderInterceptor(MessageBodyReaderInterceptor interceptor)
   {
      readerInterceptors.add(interceptor);
   }

   public MessageBodyReaderInterceptor[] bindMessageBodyReaderInterceptors(Class declaring, Method method)
   {
      List<MessageBodyReaderInterceptor> list = bindInterceptors(readerInterceptorClasses, readerInterceptors, declaring, method);
      return list.toArray(new MessageBodyReaderInterceptor[0]);
   }

   private List<Class> writerInterceptorClasses = new ArrayList<Class>();
   private List<MessageBodyWriterInterceptor> writerInterceptors = new ArrayList<MessageBodyWriterInterceptor>();

   public List<Class> getMessageBodyWriterInterceptorClasses()
   {
      return writerInterceptorClasses;
   }

   public List<MessageBodyWriterInterceptor> getMessageBodyWriterInterceptorInterceptors()
   {
      return writerInterceptors;
   }

   public void registerMessageBodyWriterInterceptor(Class clazz)
   {
      writerInterceptorClasses.add(clazz);
   }

   public void registerMessageBodyWriterInterceptors(Class[] classes)
   {
      for (Class clazz : classes) registerMessageBodyWriterInterceptor(clazz);
   }

   public void registerMessageBodyWriterInterceptor(MessageBodyWriterInterceptor interceptor)
   {
      writerInterceptors.add(interceptor);
   }

   public MessageBodyWriterInterceptor[] bindMessageBodyWriterInterceptors(Class declaring, Method method)
   {
      List<MessageBodyReaderInterceptor> list = bindInterceptors(writerInterceptorClasses, writerInterceptors, declaring, method);
      return list.toArray(new MessageBodyWriterInterceptor[0]);
   }

}