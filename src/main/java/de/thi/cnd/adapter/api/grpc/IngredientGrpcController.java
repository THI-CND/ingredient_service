package de.thi.cnd.adapter.api.grpc;

import de.thi.cnd.domain.IngredientService;
import de.thi.cnd.domain.model.Ingredient;
import de.thi.cnd.ingredient.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Optional;

@GrpcService
public class IngredientGrpcController extends IngredientServiceGrpc.IngredientServiceImplBase {

    private final IngredientService ingredientService;

    public IngredientGrpcController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @Override
    public void getIngredients(Empty request, StreamObserver<IngredientsResponse> responseObserver) {
        List<Ingredient> ingredients = ingredientService.getIngredients();
        IngredientsResponse.Builder responseBuilder = IngredientsResponse.newBuilder();

        for (Ingredient ingredient : ingredients) {
            IngredientResponse ingredientResponse = IngredientResponse.newBuilder()
                    .setId(ingredient.getId())
                    .setName(ingredient.getName())
                    .setUnit(ingredient.getUnit())
                    .addAllTags(ingredient.getTags())
                    .build();
            responseBuilder.addIngredients(ingredientResponse);
        }

        IngredientsResponse response = responseBuilder.build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getIngredient(IngredientIdRequest request, StreamObserver<IngredientResponse> responseObserver) {
        Optional<Ingredient> ingredient = ingredientService.getIngredientById(request.getId());

        if (ingredient.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Ingredient not found").asException());
            return;
        }

        IngredientResponse response = IngredientResponse.newBuilder()
                .setId(ingredient.get().getId())
                .setName(ingredient.get().getName())
                .setUnit(ingredient.get().getUnit())
                .addAllTags(ingredient.get().getTags())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void createIngredient(CreateIngredientRequest request, StreamObserver<IngredientResponse> responseObserver) {
        Ingredient ingredient = ingredientService.createIngredient(request.getName(), request.getUnit(), request.getTagsList());
        IngredientResponse response = IngredientResponse.newBuilder()
                .setId(ingredient.getId())
                .setName(ingredient.getName())
                .setUnit(ingredient.getUnit())
                .addAllTags(ingredient.getTags())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateIngredient(UpdateIngredientRequest request, StreamObserver<IngredientResponse> responseObserver) {
        Optional<Ingredient> ingredient = ingredientService.updateIngredient(request.getId(), request.getName(), request.getUnit(), request.getTagsList());

        if (ingredient.isEmpty()) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Ingredient not found").asException());
            return;
        }

        IngredientResponse response = IngredientResponse.newBuilder()
                .setId(ingredient.get().getId())
                .setName(ingredient.get().getName())
                .setUnit(ingredient.get().getUnit())
                .addAllTags(ingredient.get().getTags())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteIngredient(IngredientIdRequest request, StreamObserver<Empty> responseObserver) {
        ingredientService.deleteIngredient(request.getId());
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getTags(Empty request, StreamObserver<TagsResponse> responseObserver) {
        List<String> tags = ingredientService.getTags();
        TagsResponse response = TagsResponse.newBuilder().addAllTags(tags).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
