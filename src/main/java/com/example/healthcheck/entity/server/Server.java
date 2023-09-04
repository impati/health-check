package com.example.healthcheck.entity.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.healthcheck.entity.common.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "server_table")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Server extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "server_id")
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "server_name", nullable = false)
	private String serverName;

	@Column(name = "host", nullable = false)
	private String host;

	@Column(name = "path", nullable = false)
	private String path;

	@Column(name = "method", nullable = false)
	@Enumerated(value = EnumType.STRING)
	private EndPointHttpMethod method;

	@Column(name = "interval_time", nullable = false)
	private Integer interval; // minute

	@Column(name = "active", nullable = false)
	private boolean active;

	@OneToMany(mappedBy = "server", cascade = CascadeType.ALL)
	private List<QueryParam> queryParams = new ArrayList<>();

	@Builder
	public Server(
		final String serverName,
		final String email,
		final String host,
		final String path,
		final EndPointHttpMethod method,
		final MultiValueMap<String, String> params,
		final Integer interval,
		final boolean active
	) {
		this.serverName = serverName;
		this.email = email;
		this.host = host;
		this.path = path;
		this.method = method;
		this.interval = interval;
		this.active = active;
		saveQueryParams(params);
	}

	public void deactivate() {
		this.active = false;
	}

	public void activate() {
		this.active = true;
	}

	private MultiValueMap<String, String> toPrams() {
		final MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
		for (var element : queryParams) {
			multiValueMap.add(element.getKey(), element.getValue());
		}

		return multiValueMap;
	}

	public String getUrl() {
		return UriComponentsBuilder.fromHttpUrl(host)
			.path(path)
			.queryParams(toPrams())
			.build()
			.toString();
	}

	private void saveQueryParams(final MultiValueMap<String, String> params) {
		if (params == null) {
			return;
		}

		for (String key : params.keySet()) {
			saveQueryParams(key, params.get(key));
		}
	}

	private void saveQueryParams(final String key, final List<String> values) {
		for (String value : values) {
			queryParams.add(QueryParam.builder()
				.key(key)
				.value(value)
				.server(this)
				.build());
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Server server)) {
			return false;
		}

		return this.getId() != null && Objects.equals(id, server.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
