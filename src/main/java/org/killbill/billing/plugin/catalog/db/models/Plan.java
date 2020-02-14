package org.killbill.billing.plugin.catalog.db.models;

/*
 * Copyright 2014-2020 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.killbill.billing.catalog.api.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;


@JsonInclude(NON_ABSENT)
public class Plan implements SimplePlanDescriptor {

	private final UUID id;
	private final String planName;
	private final String productName;
	private final ProductCategory productCategory;
	private final Currency currency;
	private final BigDecimal amount;
	private final BillingPeriod billingPeriod;
	private final Integer trialLength;
	private final TimeUnit trialTimeUnit;
	private final List<String> availableBaseProducts;

	@JsonCreator
	public Plan(
			@JsonProperty("id") UUID id,
			@JsonProperty("planName") String planName,
			@JsonProperty("productName") String productName,
			@JsonProperty("productCategory") ProductCategory productCategory,
			@JsonProperty("currency") Currency currency,
			@JsonProperty("amount") BigDecimal amount,
			@JsonProperty("billingPeriod") BillingPeriod billingPeriod,
			@JsonProperty("trialLength") Integer trialLength,
			@JsonProperty("trialTimeUnit") TimeUnit trialTimeUnit,
			@JsonProperty("availableBaseProducts") List<String> availableBaseProducts
	) {
		this.id = id;
		this.planName = planName;
		this.productName = productName;
		this.productCategory = productCategory;
		this.currency = currency;
		this.amount = amount;
		this.billingPeriod = billingPeriod;
		this.trialLength = trialLength;
		this.trialTimeUnit = trialTimeUnit;
		this.availableBaseProducts = availableBaseProducts;
	}

	public UUID getId() {
		return id;
	}

	public String getPlanId() {
		return this.planName;
	}

	public String getProductName() {
		return this.productName;
	}

	public ProductCategory getProductCategory() {
		return this.productCategory;
	}

	public List<String> getAvailableBaseProducts() {
		return this.availableBaseProducts;
	}

	public Currency getCurrency() {
		return this.currency;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public BillingPeriod getBillingPeriod() {
		return this.billingPeriod;
	}

	public Integer getTrialLength() {
		return this.trialLength;
	}

	public TimeUnit getTrialTimeUnit() {
		return this.trialTimeUnit;
	}

	public String toString() {
		return "DatabasePlanDescriptor{planId='" + this.planName + '\'' + ", productName='" + this.productName + '\'' + ", productCategory=" + this.productCategory + ", currency=" + this.currency + ", amount=" + this.amount + ", billingPeriod=" + this.billingPeriod + ", trialLength=" + this.trialLength + ", trialTimeUnit=" + this.trialTimeUnit + ", availableBaseProducts=" + this.availableBaseProducts + '}';
	}
}
