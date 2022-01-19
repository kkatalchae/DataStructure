package list;

import java.util.Arrays;


/*
    # ArrayList

    List 인터페이스를 상속하는 자료 구조

    모든 자료 구조가 그러하듯 동적 할당을 전제로 한다. 동적 할당이란 값이 추가되거나 제거될 때 자체적으로 크기를 조정하는 것을 의미

    리스트 계열의 자료구조는 데이터 사이의 빈 공간을 허용하지 않는다. 즉, 값과 값 사이에 null 값이 있으면 안된다.

    인덱스를 통해 데이터를 가져오는 것이 빈번하다면 ArrayList 를 사용하는 것이 효율적

    하지만 데이터의 추가 / 삭제가 빈번하다면 ArrayList 는 다른 값들을 재배치하기 때문에 효율적이지 않다.

 */
public class ArrayList <E> implements List <E>{

    private static final int DEFAULT_CAPACITY = 10; // 기본 용적 크기
    private static final Object[] EMPTY_ARRAY = {}; // 빈 배열

    private int size; // 요소 개수

    Object[] array; // 요소를 담을 배열

    // 생성자 1 ( 초기 공간 할당 x )
    public ArrayList() {
        this.array = EMPTY_ARRAY;
        this.size = 0;
    }

    // 생성자 2 ( 초기 공간 할당 o )
    public ArrayList(int capacity) {
        this.array = new Object[capacity];
        this.size = 0;
    }

    // 동적 할당을 위한 resize 메소드
    private void resize(){
        int array_capacity = array.length;

        // 배열의 용량이 0 일 경우
        if (Arrays.equals(array, EMPTY_ARRAY)){
            array = new Object[DEFAULT_CAPACITY];
            return;
        }

        // 용량이 꽉 찰 경우
        if (size == array_capacity) {
            int new_capacity = array_capacity * 2;

            array = Arrays.copyOf(array, new_capacity);
            return;
        }

        // 용적의 절반 미만으로 요소가 차지하고 있을 경우
        if (size < (array_capacity / 2)) {
            int new_capacity = array_capacity / 2;

            array = Arrays.copyOf(array, new_capacity);
            return;
        }
    }

    // 배열의 마지막에 인자로 받은 값을 할당해주는 메소드
    public void addLast(E value) {

        // 배열이 가득 차있는 상태라면 크기를 늘려줌
        if (size == array.length){
            resize();
        }
        array[size] = value;
        size++;
    }

    // 자바에서 구현하고 있는 add 메소드는 배열의 마지막에 값을 할당하는 것이므로 addLast를 구현
    @Override
    public boolean add(E value) {
        addLast(value);
        return true;
    }

    // 특정 위치에 값을 위치시키기 위해 add 메소드를 오버로딩
    @Override
    public void add(int index, E value) {

        if (index > size || index < 0){ // 범위를 벗어날 경우 예외 발생
            throw new IndexOutOfBoundsException();
        }

        if (index == size) { // 위치시키고 싶은 위치가 마지막이라면 addLast 메소드 재활용
            addLast(value);
        } else {
            if (size == array.length) { // 배열이 꽉차있다면 용적 재할당
                resize();
            }

            // index 기준 뒤에 있는 모든 요소들 한 칸씩 뒤로 이동
            for (int i = size; i > index; i--) {
                array[i] = array[i - 1];
            }

            array[index] = value;
            size++;
        }
    }

    // 특정 위치에 있는 값을 알아내기 위한 get 메소드
    @SuppressWarnings("unchecked")
    /* Object -> E 타입으로 변환할 때 변환할 수 없는 타입일 가능성이 있음. 하지만 우리는 add 하는 값들에 대해 E 타입만 허용하고
     있기 때문에 형 안정성이 보장되므로 경고를 무시하는 SuppressWarning 사용 */
    @Override
    public E get(int index) {
        if (index >= size || index < 0) { // 범위 벗어나면 예외 발생
            throw new IndexOutOfBoundsException();
        }

        // Object 타입에서 E 타입으로 캐스팅 후 반환
        return (E) array[index];
    }

    // 특정 위치에 존재하는 값을 교체해주기 위한 set 메소드
    @Override
    public void set(int index, E value) {
        if (index >= size || index < 0) { // 범위 벗어나면 예외 발생
            throw new IndexOutOfBoundsException();
        } else {
            array[index] = value;
        }
    }

    // 찾고자 하는 값의 위치를 반환하는 indexOf 메소드 ( 단, 중복되는 값일 경우 가장 앞을 반환하고 없을 경우 -1 반환 )
    @Override
    public int indexOf(Object value) {
        int i = 0;

        // 인자와 같은 객체( 요소 값 ) 일 경우 위치 반환
        for (i = 0; i < size; i++){
            if (array[i].equals(value)) {
                return i;
            }
        }

        // 찾는 값이 존재하지 않을 경우
        return -1;
    }

    // 찾고자 하는 값이 자료 구조에 존재하는 지 확인하는 contains 메소드
    @Override
    public boolean contains(Object value) {

        // 0 이상이면 요소가 존재한다는 의미
        if (indexOf(value) >= 0)
            return true;
        else
            return false;
    }

    // 특정 위치의 값을 없애기 위한 remove 메소드
    @SuppressWarnings("unchecked")
    @Override
    public E remove(int index) {

        if (index >= size || index < 0) { // 범위 벗어나면 예외 발생
            throw new IndexOutOfBoundsException();
        }

        E element = (E) array[index]; // 삭제될 요소를 반환하기 위해 임시로 담아둠
        array[index] = null;

        // 삭제한 요소의 뒤에 있는 모든 요소들을 한 칸씩 앞으로 이동
        for (int i = index; i < size; i++) {
            array[i] = array[i + 1];
            array[i + 1] = null;
        }

        size--;
        resize();
        return element;
    }

    // 원하는 값을 제거하기 위한 remove 메소드 오버로딩
    @Override
    public boolean remove(Object value) {

        // 삭제하고자 하는 요소의 위치 찾기
        int index = indexOf(value);

        // -1 은 원하는 값이 자료 구조에 없다는 의미기 때문에 제거에 실패 false 리턴
        if (index == -1) return false;

        // 앞서 구현했던 remove 메소드를 재활용해서 해당 값을 지워준다.
        remove(index);
        return true;
    }

    // 자료구조의 크기를 알기 위한 size 메소드
    @Override
    public int size() {
        return size;
    }

    // 자료구조가 비어있는 지 확인하기 위한 isEmpty 메소드
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 자료 구조에 있는 모든 값들을 없애는 clear 메소드
    @Override
    public void clear() {

        // 모든 값들을 null 처리
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }

        size = 0;
        resize();
    }
}
