/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 *  obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.emrmonitor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration methods used by the Emr Monitor module
 */
public class EmrMonitorConfig {

    protected static Log log = LogFactory.getLog(EmrMonitorConfig.class);

    // Privileges
    public static final String PRIV_MANAGE_EMR_MONITOR = "Manage EmrMonitor";

    // Constants
    public static final Integer REMOTE_SERVER_TIMEOUT = 10000;

    // Runtime Properties
    public static final String PARENT_URL_PROPERTY = "emrmonitor.parentUrl";
    public static final String PARENT_USERNAME_PROPERTY = "emrmonitor.parentUsername";
    public static final String PARENT_PASSWORD_PROPERTY = "emrmonitor.parentPassword";

    // Static variables for holding the runtime configuration

    private static String parentUrl = getRuntimeProperty(PARENT_URL_PROPERTY, null);
    private static String parentUsername = getRuntimeProperty(PARENT_USERNAME_PROPERTY, null);
    private static String parentPassword = getRuntimeProperty(PARENT_PASSWORD_PROPERTY, null);

    /**
     * @return true if a parent server is configured
     */
    public static boolean isParentServerConfigured() {
        return parentUrl != null && parentUsername != null && parentPassword != null;
    }

    public static String getEmrMonitorParentUrl() {
        return parentUrl;
    }

    public static String getEmrMonitorParentUsername() {
        return parentUsername;
    }

    public static String getEmrMonitorParentPassword() {
        return parentPassword;
    }

    public static void setEmrMonitorParentUrl(String url) {
        parentUrl = url;
    }

    public static void setEmrMonitorParentUsername(String username) {
        parentUsername = username;
    }

    public static void setEmrMonitorParentPassword(String password) {
        parentPassword = password;
    }

    public static String getRuntimeProperty(String name, String defaultValue) {
        String value = Context.getRuntimeProperties().getProperty(name);
        return StringUtils.isNotBlank(value) ? value : defaultValue;
    }

    // Global Properties
    public static final String GP_DISABLED_METRIC_PRODUCERS = "emrmonitor.disabledMetricProducers";
    public static final String GP_MINUTES_BETWEEN_REPORTS = "emrmonitor.minutesBetweenReports";

    public static List<String> getDisabledMetricProducers() {
        List<String> ret = new ArrayList<String>();
        String val = Context.getAdministrationService().getGlobalProperty(GP_DISABLED_METRIC_PRODUCERS);
        if (StringUtils.isNotBlank(val)) {
            for (String s : StringUtils.splitByWholeSeparator(val, ",")) {
                ret.add(s.trim());
            }
        }
        return ret;
    }

    public static int getMinutesBetweenReports() {
        int ret = 60*24; // By default, run reports daily
        try {
            String val = Context.getAdministrationService().getGlobalProperty(GP_MINUTES_BETWEEN_REPORTS);
            if (StringUtils.isNotBlank(val)) {
                ret = Integer.parseInt(val);
            }
        }
        catch (Exception e) {
            log.warn("Invalid configuration for global property: " + GP_MINUTES_BETWEEN_REPORTS + ", using default");
        }
        return ret;
    }
}
