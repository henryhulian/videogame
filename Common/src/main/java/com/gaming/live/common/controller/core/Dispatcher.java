package com.gaming.live.common.controller.core;

import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;

public interface Dispatcher<RQ extends ApplicationRequest,RP extends ApplicationResponse> {
	RP dispatcher ( RQ request );
}
