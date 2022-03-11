package sn.free.selfcare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Operation Microservice.
 * <p>
 * Properties are configured in the {@code application.yml} file. See
 * {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

	private final EmployeAlert employeAlert = new EmployeAlert();

	private final Payment payment = new Payment();

	private final Sftp sftp = new Sftp();

	private final FactureConf factureConf = new FactureConf();

	/**
	 * <p>
	 * Getter for the field <code>employeAlert</code>.
	 * </p>
	 *
	 * @return a {@link sn.free.selfcare.config.ApplicationProperties.EmployeAlert}
	 *         object.
	 */
	public EmployeAlert getEmployeAlert() {
		return employeAlert;
	}

	/**
	 * <p>
	 * Getter for the field <code>payment</code>.
	 * </p>
	 *
	 * @return a {@link sn.free.selfcare.config.ApplicationProperties.Payment}
	 *         object.
	 */
	public Payment getPayment() {
		return payment;
	}

	/**
	 * <p>
	 * Getter for the field <code>factureConf</code>.
	 * </p>
	 *
	 * @return a {@link sn.free.selfcare.config.ApplicationProperties.FactureConf}
	 *         object.
	 */
	public FactureConf getFactureConf() {
		return factureConf;
	}

	/**
	 * <p>
	 * Getter for the field <code>sftp</code>.
	 * </p>
	 *
	 * @return a {@link sn.free.selfcare.config.ApplicationProperties.Sftp} object.
	 */
	public Sftp getSftp() {
		return sftp;
	}

	public static class EmployeAlert {

		private String expression = "0 0 6,19 * * *";

		public String getExpression() {
			return expression;
		}

		public void setExpression(String expression) {
			this.expression = expression;
		}
	}

	public static class Payment {

		private String url;

		private String b2bmsisdn;

		private String b2buserName;

		private String b2bPassword;

		public String getB2bmsisdn() {
			return b2bmsisdn;
		}

		public void setB2bmsisdn(String b2bmsisdn) {
			this.b2bmsisdn = b2bmsisdn;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getB2buserName() {
			return b2buserName;
		}

		public void setB2buserName(String b2buserName) {
			this.b2buserName = b2buserName;
		}

		public String getB2bPassword() {
			return b2bPassword;
		}

		public void setB2bPassword(String b2bPassword) {
			this.b2bPassword = b2bPassword;
		}
	}

	public static class Sftp {
		private String host;

		private Integer port;

		private String username;

		private String password;

		private Integer sessionTimeout;

		private Integer channelTimeout;

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Integer getSessionTimeout() {
			return sessionTimeout;
		}

		public void setSessionTimeout(Integer sessionTimeout) {
			this.sessionTimeout = sessionTimeout;
		}

		public Integer getChannelTimeout() {
			return channelTimeout;
		}

		public void setChannelTimeout(Integer channelTimeout) {
			this.channelTimeout = channelTimeout;
		}
	}

	public static class FactureConf {
		private String rootPath;

		public String getRootPath() {

			return rootPath;
		}

		public void setRootPath(String rootPath) {
			this.rootPath = rootPath;
		}
	}
}
