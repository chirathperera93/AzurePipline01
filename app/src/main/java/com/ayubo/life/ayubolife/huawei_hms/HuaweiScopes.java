package com.ayubo.life.ayubolife.huawei_hms;


import com.huawei.hms.hihealth.data.Scopes;
import com.huawei.hms.support.api.entity.auth.Scope;

public class HuaweiScopes {

    public Scope getHealthKitBoth() {
        return new Scope(Scopes.HEALTHKIT_STEP_BOTH);
    }

    public Scope getHealthKitWeightBoth() {
        return new Scope(Scopes.HEALTHKIT_HEIGHTWEIGHT_BOTH);
    }

    public Scope getHealthKitHeartRateBoth() {
        return new Scope(Scopes.HEALTHKIT_HEARTRATE_BOTH);
    }

    public Scope getHealthKitStepRealTime() {
        return new Scope(Scopes.HEALTHKIT_STEP_REALTIME);
    }
}
