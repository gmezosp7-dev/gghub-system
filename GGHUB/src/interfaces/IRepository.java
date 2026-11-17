package interfaces;

import java.util.ArrayList;


//La <T> es el tipo de entidad (Cliente, Torneo...)
public interface IRepository<T> {

    ArrayList<T> getDataList();

    void insertData(T entity);

    void updateData(T oldEntity, T newEntity);

    void deleteData(T entity);

    String getDataLine(T entity);
}
