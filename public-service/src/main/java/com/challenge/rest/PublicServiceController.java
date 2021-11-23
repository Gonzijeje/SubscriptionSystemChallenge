package com.challenge.rest;

import com.challenge.PublicServiceApplication;
import com.challenge.config.ApiTag;
import com.challenge.exception.SubscriptionNotFoundException;
import com.challenge.model.EventSubscription;
import com.challenge.model.Subscription;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Rest controller for the application
 * <p>
 * This class exposes to other services the different endpoints of application resource 'Subscription'
 * </p>
 *
 * @author Gonzalo Collada
 * @version 1.0.0
 */
@Api(tags = ApiTag.SUBSCRIPTIONS)
@RestController(value = "Subscriptions")
@RequestMapping(value = "/subscriptions")
public class PublicServiceController {

    //local store name
    private static final String STORE_NAME = PublicServiceApplication.STORE_NAME;
    //Object to keep stored all key-value record on RocksDB Kafka broker
    ReadOnlyKeyValueStore<String, Subscription> keyValueStore;

    @Autowired
    private InteractiveQueryService queryService;

    @Autowired
    private KafkaTemplate<String, EventSubscription> kafkaTemplate;

    private static final Logger LOG = LoggerFactory.getLogger(PublicServiceController.class);

    @ApiOperation("Return a list with the subscriptions")
    @ApiResponse(code = 200, message = "successful operation", response = Subscription.class)
    @GetMapping(produces = { "application/json" })
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        keyValueStore = queryService.getQueryableStore(STORE_NAME, QueryableStoreTypes.<String, Subscription>keyValueStore());
        KeyValueIterator<String, Subscription> kvi = keyValueStore.all();

        List<Subscription> subscriptions = new ArrayList<Subscription>();
        kvi.forEachRemaining( k -> subscriptions.add(k.value));

        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @ApiOperation("Return the detail of a subscription")
    @ApiResponses({
            @ApiResponse(code = 200, message = "successful operation", response = Subscription.class),
            @ApiResponse(code = 404, message = "subscription not found")
    })
    @GetMapping(value = "/{id}", produces = { "application/json" })
    public ResponseEntity<Subscription> getOneSubscription(@PathVariable @NotNull String id) throws SubscriptionNotFoundException {
        keyValueStore = queryService.getQueryableStore(STORE_NAME, QueryableStoreTypes.<String, Subscription>keyValueStore());
        KeyValueIterator<String, Subscription> kvi = keyValueStore.all();
        Subscription subscription = new Subscription();
        while(kvi.hasNext()){
            KeyValue<String, Subscription> kv = kvi.next();
            if (kv.value.getId().matches(id)){
                subscription = kv.value;
                return new ResponseEntity<>(subscription, HttpStatus.OK);
            }
        }
        throw new SubscriptionNotFoundException(id);
    }

    @ApiOperation("Create a new subscription")
    @ApiResponse(code = 201, message = "subscription created")
    @PostMapping(consumes = { "application/json" })
    public ResponseEntity createSubscription(@RequestBody @Validated Subscription subscription){
        EventSubscription event = new EventSubscription("CREATE", subscription);
        kafkaTemplate.send("event-subscription", event);
        LOG.info("Subscription created: " + subscription.toString());
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ApiOperation("Delete an existing subscription")
    @ApiResponses({
            @ApiResponse(code = 202, message = "subscription removed"),
            @ApiResponse(code = 404, message = "subscription not found")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity deleteSubscription(@PathVariable @NotNull String id) throws SubscriptionNotFoundException {
        keyValueStore = queryService.getQueryableStore(STORE_NAME, QueryableStoreTypes.<String, Subscription>keyValueStore());
        KeyValueIterator<String, Subscription> kvi = keyValueStore.all();
        while(kvi.hasNext()){
            KeyValue<String, Subscription> kv = kvi.next();
            if (kv.value.getId().matches(id)){
                EventSubscription event = new EventSubscription("DELETE", null);
                kafkaTemplate.send("event-subscription",id, event);
                LOG.info(String.format("Delete Subscription with id: %s",id));
                return new ResponseEntity(HttpStatus.ACCEPTED);
            }
        }
        throw new SubscriptionNotFoundException(id);
    }
}
