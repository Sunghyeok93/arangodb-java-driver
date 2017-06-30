/*
 * DISCLAIMER
 *
 * Copyright 2016 ArangoDB GmbH, Cologne, Germany
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Copyright holder is ArangoDB GmbH, Cologne, Germany
 */

package com.arangodb.entity;

import java.util.Collection;

/**
 * @author Mark Vollmary
 *
 */
public class MultiDocumentEntity<E extends DocumentEntity> {

	private Collection<E> documents;
	private Collection<ErrorEntity> errors;

	public MultiDocumentEntity() {
		super();
	}

	public Collection<E> getDocuments() {
		return documents;
	}

	public void setDocuments(final Collection<E> documents) {
		this.documents = documents;
	}

	public Collection<ErrorEntity> getErrors() {
		return errors;
	}

	public void setErrors(final Collection<ErrorEntity> errors) {
		this.errors = errors;
	}

}
