import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class Heap <E> {

    /*
        # 힙 ( Heap )

        힙은 최솟값 또는 최댓값을 빠르게 찾아내기 위해 완전이진트리 형태로 만들어진 자료구조를 말한다.

        완전이진트리는 모든 노드의 최대 차수를 2 로 제한한 이진 트리의 일종이며 다음과 같은 조건을 만족해야 한다.

        1. 마지막 레벨을 제외한 모든 노드가 채워져 있어야 한다.
        2. 모든 노드들은 왼쪽부터 채워져 있어야 한다.

        힙은 이러한 완전이진트리의 규칙에 부모 노드는 항상 자식 노드보다 우선 순위가 높다라는 조건이 추가되어있다.

        시간 복잡도 : O(1) - 최댓값, 최솟값 / O(logN) - 삽입, 삭제 연산

        특징 : 느슨한 정렬 ( 반 정렬 ), 힙 트리에서는 중복된 값을 허용한다.

        종류 : 최소 힙 ( 최솟값이 루트에 위치 ), 최대 힙 ( 최댓값이 루트에 위치 )

        성질
        1. 왼쪽 자식 노드 인덱스 = 부모 노드 인덱스 * 2
        2. 오른쪽 자식 노드 인덱스 = 부모 노드 인덱스 * 2 + 1
        3. 부모 노드 인덱스 = 자식 노드 인덱스 / 2

     */

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 10; // 기본 크기

    private int size; // 데이터 갯수

    private Object[] array; // 힙을 구현하는 데 사용하는 배열

    // 생성자
    public Heap() {
        this(null);
    }

    public Heap(Comparator<? super E> comparator) {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    public Heap(int capacity) {
        this(capacity, null);
    }

    public Heap(int capacity, Comparator<? super E> comparator) {
        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    // 부모 노드의 인덱스를 가져오는 메소드
    private int getParent(int index) {
        return index / 2;
    }

    // 왼쪽 자식 노드의 인덱스를 가져오는 메소드
    private int getLeftChild(int index) {
        return index * 2;
    }

    // 오른쪽 자식 노드의 인덱스를 가져오는 메소드
    private int getRightChild(int index) {
        return index * 2 + 1;
    }

    // 동적 할당을 위한 resize 메소드
    private void resize(int newCapacity) {

        // 크기를 조정할 때는 새로운 배열을 만들어 옮겨준다.
        Object[] newArray = new Object[newCapacity];

        // 힙은 배열의 1 번 인덱스부터 사용한다.
        for (int i = 1; i <= size; i++) {
            newArray[i] = array[i];
        }

        this.array = null; // 기존 배열 삭제
        this.array = newArray; // 새로 만든 배열로 교체
    }


    // 트리의 마지막 노드에 데이터를 추가하고 힙 규칙에 따라 재배치 ( siftUp )
    public void add(E value) {

        if (size + 1 == array.length) resize(array.length * 2);

        siftUp(size + 1, value);
        size++;
    }

    // Comparator 가 존재할 경우와 존재하지 않는 경우로 나누어서 처리
    private void siftUp(int idx, E target) {

        if (comparator != null) {
            siftUpComparator(idx, target, comparator);
        }
        else {
            siftUpComparable(idx, target);
        }
    }

    private void siftUpComparator(int idx, E target, Comparator<? super E> comp) {

        while (idx > 1) { // 힙의 루트 노드까지 살피도록 조건 설정
            int parent = getParent(idx);
            Object parentVal = array[parent];

            if (comp.compare(target, (E) parentVal) >= 0) { // 입력된 값이 부모보다 크면 반복문 종료
                break;
            }

            /*
                부모노드가 입력된 값보다 크므로
                현재 삽입 될 위치에 부모 노드값이 위치하도록 교체해주고
                타겟 노드의 위치를 부모노드의 위치로 변경해준다.
             */
            array[idx] = parentVal;
            idx = parent;
        }

        // 최종적으로 삽입될 위치에 타겟 노드 값을 저장
        array[idx] = target;
    }

    private void siftUpComparable(int idx, E target) {

        // target 노드가 비교 될 수 있도록 한 변수를 만들어준다.
        Comparable<? super E> comp = (Comparable<? super E>) target;

        while (idx > 1) {
            int parent = getParent(idx);
            Object parentVal = array[parent];

            if (comp.compareTo((E) parentVal) >= 0) {
                break;
            }

            array[idx] = parentVal;
            idx = parent;
        }

        array[idx] = comp;
    }

    public E remove() {

        // 루트가 비어있을 경우 예외 발생
        if (array[1] == null) throw new NoSuchElementException();

        E result = (E) array[1]; // 삭제된 요소를 반환하기 위한 변수 생성
        E target = (E) array[size]; // 타겟이 될 요소
        array[size] = null; // 타겟 노드를 비운다.

        // 가장 마지막 노드를 루트로 보낸 뒤, 아래로 가면서 힙의 규칙에 맞게 재배치
        siftDown(1, target);

        return result;
    }

    private void siftDown(int idx, E target) {

        if (comparator != null) {
            siftDownComparator(idx, target, comparator);
        }
        else {
            siftDownComparable(idx, target);
        }
    }

    private void siftDownComparator(int idx, E target, Comparator<? super E> comp) {

        array[idx] = null; // 루트에 있는 값을 삭제
        size--;

        int parent = idx;
        int child;

        // 왼쪽 자식 노드의 인덱스가 요소의 개수보다 작을 때까지 반복
        while ((child = getLeftChild(parent)) <= size) {

            int right = getRightChild(parent); // 오른쪽 자식 인덱스

            Object childVal = array[child]; // 왼쪽 자식 노드의 값 ( 교환될 값 )


            /*
                오른쪽 자식 인덱스가 size 를 넘지 않으면서 왼쪽 자식이 오른쪽 자식보다 큰 경우
                재배치할 노드는 작은 자식과 비교해야 하므로 child 와 childVal 을 오른쪽 자식으로 교체
             */
            if (right <= size && comp.compare((E) childVal, (E) array[right]) > 0) {
                child = right;
                childVal = array[child];
            }

            // 재배치할 노드가 자식 노드보다 작을 경우 반복문 종료
            if (comp.compare(target, (E) childVal) <= 0) {
                break;
            }

            array[parent] = childVal;
            parent = child;
        }

        array[parent] = target;

        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
    }

    // 위의 메소드와 원리는 동일 
    private void siftDownComparable(int idx, E target) {
        Comparable<? super E> comp = (Comparable<? super E>) target;

        array[idx] = null;
        size--;

        int parent = idx;
        int child;

        while((child = getLeftChild(parent)) <= size) {
            int right = getRightChild(parent);

            Object childVal = array[child];

            if (right <= size && ((Comparable<? super E>) childVal).compareTo((E)array[right]) >0) {
                child = right;
                childVal = array[child];
            }

            if (comp.compareTo((E) childVal) <= 0) {
                break;
            }

            array[parent] = childVal;
            parent = child;

        }

        array[parent] = comp;

        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }
    }

    // 이하 설명 생략
    public int size() {
        return this.size;
    }

    public E peek() {
        if (array[1] == null) throw new NoSuchElementException();

        return (E) array[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Object[] toArray() {
        return Arrays.copyOf(array, size + 1);
    }


}
