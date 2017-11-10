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

package com.arangodb;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.arangodb.entity.CursorEntity;
import com.arangodb.entity.CursorEntity.Extras;
import com.arangodb.entity.CursorEntity.Stats;
import com.arangodb.entity.CursorEntity.Warning;
import com.arangodb.internal.ArangoCursorExecute;
import com.arangodb.internal.ArangoCursorIterator;
import com.arangodb.internal.InternalArangoDatabase;
import com.arangodb.internal.net.HostHandle;

/**
 * @author Mark Vollmary
 *
 */
public class ArangoCursor<T> implements Iterable<T>, Iterator<T>, Closeable {

	private final Class<T> type;
	protected final ArangoCursorIterator<T> iterator;
	private final String id;
	private final ArangoCursorExecute execute;
	private final HostHandle hostHandle;

	protected ArangoCursor(final InternalArangoDatabase<?, ?, ?, ?> db, final ArangoCursorExecute execute,
		final Class<T> type, final CursorEntity result) {
		super();
		this.execute = execute;
		this.type = type;
		hostHandle = new HostHandle();
		iterator = createIterator(this, db, execute, result, hostHandle);
		id = result.getId();
	}

	protected ArangoCursorIterator<T> createIterator(
		final ArangoCursor<T> cursor,
		final InternalArangoDatabase<?, ?, ?, ?> db,
		final ArangoCursorExecute execute,
		final CursorEntity result,
		final HostHandle hostHandle) {
		return new ArangoCursorIterator<T>(cursor, execute, db, result, hostHandle);
	}

	/**
	 * @return id of temporary cursor created on the server
	 */
	public String getId() {
		return id;
	}

	public Class<T> getType() {
		return type;
	}

	/**
	 * @return the total number of result documents available (only available if the query was executed with the count
	 *         attribute set)
	 */
	public Integer getCount() {
		return iterator.getResult().getCount();
	}

	public Stats getStats() {
		final Extras extra = iterator.getResult().getExtra();
		return extra != null ? extra.getStats() : null;
	}

	public Collection<Warning> getWarnings() {
		final Extras extra = iterator.getResult().getExtra();
		return extra != null ? extra.getWarnings() : null;
	}

	/**
	 * @return indicating whether the query result was served from the query cache or not
	 */
	public boolean isCached() {
		final Boolean cached = iterator.getResult().getCached();
		return cached != null && cached.booleanValue();
	}

	@Override
	public void close() throws IOException {
		if (id != null) {
			execute.close(id, hostHandle);
		}
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public T next() {
		return iterator.next();
	}

	public List<T> asListRemaining() {
		final List<T> remaining = new ArrayList<T>();
		while (hasNext()) {
			remaining.add(next());
		}
		return remaining;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<T> iterator() {
		return iterator;
	}

}
