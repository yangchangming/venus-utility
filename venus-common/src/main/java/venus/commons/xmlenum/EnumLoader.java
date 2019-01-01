/*
 * Copyright (c) 2006 UFIDA Software Engineering Co, Inc.
 * All Rights Reserved
 *
 * Created date 2006-11-5
 * @author changming.y
 **/

package venus.commons.xmlenum;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;
import org.apache.commons.digester.xmlrules.FromXmlRuleSet;
import venus.frames.base.exception.BaseApplicationException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Enum Defination file loader
 */
public class EnumLoader {
// ------------------------------ FIELDS ------------------------------

    // log utility
    private static Logger logger = Logger.getLogger(EnumLoader.class.getName());
    private static EnumLoader instance = null;

    /**
     * The set of public identifiers, and corresponding resource names, for
     * the versions of the configuration file DTDs that we know about.  There
     * <strong>MUST</strong> be an even number of Strings in this list!
     */
    private final static String REGISTRATION = "/venus/commons/xmlenum/enum-def.dtd";

    private final static String ENUM_RULESET_FILE = "/venus/commons/xmlenum/enum-ruleset.xml";

    private Digester digester = null;
    
//  -------------------------- STATIC METHODS --------------------------

    /**
     * get singleton EnumRepository object
     *
     * @return singleton EnumRepository object
     */
    public static EnumLoader getInstance() {
        if (instance == null) {
            synchronized (EnumLoader.class) {
                if (instance == null) {
                    instance = new EnumLoader();
                }
            }
        }

        return instance;
    }    

// --------------------------- CONSTRUCTORS ---------------------------

    private EnumLoader() {
        digester = createDigester();
    }

    /**
     * initialize the config digester object
     *
     * @return digester object
     */
    private Digester createDigester() {

        // try load application config xml
        Digester digester = new Digester();
        digester.setNamespaceAware(true);
        digester.setValidating(false);
        digester.setUseContextClassLoader(true);

        RuleSet ruleset = getDigesterRuleset();
        if (ruleset != null) {
            digester.addRuleSet(ruleset);
            URL url = this.getClass().getResource(REGISTRATION);
            digester.register( REGISTRATION, url.toString() );
        }

        return digester;
    }

    /**
     * Initialize the ruleset using xml
     *
     * @return
     */
    private RuleSet getDigesterRuleset() {

        URL rulesetURL = this.getClass().getResource(ENUM_RULESET_FILE);
        

        if (rulesetURL == null) {
            logger.severe("Can't initialize ruleset, AOClassMetadata digester init failed.");
            return null;
        }

        FromXmlRuleSet ruleset = new FromXmlRuleSet(rulesetURL);
        return ruleset;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Load AO meta data from resource path
     *
     * @param path ao resource path
     * @return enum object loaded
     * @throws ResourceException , if the metadata config file is not valid or can't be found
     */
    public EnumValueMap loadEnumDefination(String path) throws BaseApplicationException {
        InputStream is = null;

        try {
            is = this.getClass().getResourceAsStream(path);
            return loadEnumDefination(is);
        } catch (Exception e) {
            String msg = "Error parsing AOMetaConfig file '" + path + "'.";
            logger.log(Level.SEVERE, msg, e);
            throw new BaseApplicationException(msg);
        } finally {
            try {
				is.close();
			} catch (IOException e1) {
				logger.log(Level.SEVERE,"Close is failed.", e1);
			}
        }
    }

    /**
     * load enum value map from input stream
     *
     * @param is inputstream
     * @return EnumValueMap object created
     */
    public EnumValueMap loadEnumDefination(InputStream is) throws BaseApplicationException {
        EnumValueMap em = null;
        try {
            digester.clear();          // reset object stack
            em = new EnumValueMap();
            digester.push(em);

            if (is != null) {
                digester.parse(is);
            } else {
                String msg = "Can't load enum definition from null inputstream ,load enum failed.";
                logger.log(Level.SEVERE, msg);
                throw new BaseApplicationException(msg);
            }
        } catch (Exception e) {
            String msg = "Error parsing enum definition file from input stream";
            logger.log(Level.SEVERE, msg, e);
            throw new BaseApplicationException(msg);
        }

        digester.clear();
        return em;
    }

    /**
     * handy method to load enum
     * @param baseClass base class
     * @param res res name
     * @return enum def
     * @throws BaseApplicationException
     */
    public static EnumValueMap loadEnum(Class baseClass,String res) throws BaseApplicationException {

        EnumValueMap genum = null;

        // init the service enum
		EnumLoader loader = new EnumLoader();
		genum = loader.loadEnumDefination(baseClass.getClass().getResource(res).toString());
		loader = null;

        return genum;
    }

    /**
     * handy method to load enum
     * @param path path to the resource
     * @return enum def
     * @throws BaseApplicationException
     */
    public static EnumValueMap loadEnum(String path) {

        EnumValueMap genum = null;

        // init the service enum
        EnumLoader loader = new EnumLoader();
        genum = loader.loadEnumDefination(path);
        loader = null;

        return genum;
    }
}

