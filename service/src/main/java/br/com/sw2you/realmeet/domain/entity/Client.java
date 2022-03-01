package br.com.sw2you.realmeet.domain.entity;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Client {
    @Id
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    public Client() {}

    private Client(ClientBuilder clientBuilder) {
        apiKey = clientBuilder.apiKey;
        description = clientBuilder.description;
        active = clientBuilder.active;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return (
            Objects.equals(apiKey, client.apiKey) &&
            Objects.equals(description, client.description) &&
            Objects.equals(active, client.active)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(apiKey, description, active);
    }

    @Override
    public String toString() {
        return (
            "Client{" + "apiKey='" + apiKey + '\'' + ", description='" + description + '\'' + ", active=" + active + '}'
        );
    }

    public static ClientBuilder newClientBuilder() {
        return new ClientBuilder();
    }

    public static final class ClientBuilder {
        private String apiKey;
        private String description;
        private Boolean active;

        private ClientBuilder() {}

        public ClientBuilder apiKey(String apiKey) {
            this.apiKey = apiKey;
            return this;
        }

        public ClientBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ClientBuilder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Client build() {
            return new Client(this);
        }
    }
}
