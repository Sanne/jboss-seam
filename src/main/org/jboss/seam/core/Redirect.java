package org.jboss.seam.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.jboss.seam.Component;
import org.jboss.seam.InterceptionType;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Intercept;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.contexts.Contexts;

/**
 * Convenient API for performing browser redirects with
 * parameters.
 * 
 * @author Gavin King
 */
@Name("redirect")
@Intercept(InterceptionType.NEVER)
@Scope(ScopeType.CONVERSATION)
public class Redirect implements Serializable
{
   private String viewId;
   private Map<String, Object> parameters = new HashMap<String, Object>();
   private boolean conversationPropagationEnabled = true;
   
   /**
    * Get the JSF view id to redirect to
    */
   public String getViewId()
   {
      return viewId;
   }
   
   /**
    * Set the JSF view id to redirect to
    * 
    * @param viewId any JSF view id
    */
   public void setViewId(String viewId)
   {
      this.viewId = viewId;
   }
   
   /**
    * Get all the request parameters that have been set
    */
   public Map<String, Object> getParameters()
   {
      return parameters;
   }
   
   /**
    * Set a request parameter value
    */
   public void setParameter(String name, Object value)
   {
      parameters.put(name, value);
   }
   
   /**
    * Capture the view id and request parameters from the
    * current request and squirrel them away so we can
    * return here later in the conversation.
    */
   public void captureCurrentRequest()
   {
      parameters.clear();
      FacesContext context = FacesContext.getCurrentInstance();
      parameters.putAll( context.getExternalContext().getRequestParameterMap() );
      viewId = context.getViewRoot().getViewId();
   }
   
   /**
    * Should the conversation be propagated across the redirect?
    * @return true by default
    */
   public boolean isConversationPropagationEnabled()
   {
      return conversationPropagationEnabled;
   }
   
   /**
    * Note that conversations are propagated by default
    */
   public void setConversationPropagationEnabled(boolean conversationPropagationEnabled)
   {
      this.conversationPropagationEnabled = conversationPropagationEnabled;
   }
   
   /**
    * Perform the redirect
    */
   public void execute()
   {
      Manager.instance().redirect(viewId, parameters, conversationPropagationEnabled);
   }
   
   public static Redirect instance()
   {
      if ( !Contexts.isConversationContextActive() )
      {
         throw new IllegalStateException("No active conversation context");
      }
      return (Redirect) Component.getInstance(Redirect.class, ScopeType.CONVERSATION, true);
   }

}
