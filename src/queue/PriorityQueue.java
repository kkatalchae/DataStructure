package queue;

import java.util.Comparator;
import java.util.NoSuchElementException;

public class PriorityQueue<E> implements QueueInterface<E> {

    /*
        # 우선순위 큐 ( Priority Queue )

        우선순위 큐는 각 요소들이 각각의 우선순위를 갖고 있으며

        대기열에서 우선순위가 높은 요소가 낮은 요소보다 먼저 제공되는 자료구조를 말한다.

        우선순위 큐는 대체적으로 힙 ( Heap ) 자료구조를 기반으로 구현된다.

        힙은 최솟값 또는 최댓값을 빠르게 찾아내기 위해 완전이진트리 형태로 만들어진 자료구조였다.

        우선순위 큐는 중점이 되는 것이 최솟값, 최댓값이 아니라 우선순위에 중점을 준다는 점에서 차이를 보인다.

        구현하는 내용은 변수명이나 메소드명 등이 다를 뿐 기본적으로 힙과 대동소이하기 때문에 설명은 생략한다.
     */

    private final Comparator<? super E> comparator;
    private static final int DEFAULT_CAPACITY = 10;

    private int size;
    private Object[] array;

    public PriorityQueue() {
        this(null);
    }

    public PriorityQueue(Comparator<? super E> comparator) {
        this.array = new Object[DEFAULT_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    public PriorityQueue(int capacity) {
        this(capacity, null);
    }

    public PriorityQueue(int capacity, Comparator<? super E> comparator) {
        this.array = new Object[capacity];
        this.size = 0;
        this.comparator = comparator;
    }

    private int getParent(int index) {
        return index / 2;
    }

    private int getLeftChild(int index) {
        return index * 2;
    }

    private int getRightChild(int index) {
        return index * 2 + 1;
    }

    private void resize(int newCapacity) {
        Object[] newArray = new Object[newCapacity];

        for (int i = 1; i <= size; i++) {
            newArray[i] = array[i];
        }

        this.array = null;
        this.array = newArray;
    }

    @Override
    public boolean offer(E value) {

        if (size + 1 == array.length) {
            resize(array.length * 2);
        }

        siftUp(size + 1, value);
        size++;
        return true;
    }

    private void siftUp(int idx, E target) {
        /*
         * comparator가 존재한다면 comparator을 넘겨주고,
         * 아닐경우 Comparable로 비교하도록 분리해준다.
         */
        if (comparator != null) {
            siftUpComparator(idx, target, comparator);
        } else {
            siftUpComparable(idx, target);
        }
    }

    // Comparator을 이용한 sift-up
    @SuppressWarnings("unchecked")
    private void siftUpComparator(int idx, E target, Comparator<? super E> comp) {

        // root노드보다 클 때 까지만 탐색한다.
        while (idx > 1) {
            int parent = getParent(idx);    // 삽입노드의 부모노드 인덱스 구하기
            Object parentVal = array[parent];    // 부모노드의 값

            // 타겟 노드 우선순위(값)이 부모노드보다 크면 반복문 종료
            if (comp.compare(target, (E) parentVal) >= 0) {
                break;
            }
            /*
             * 부모노드가 타겟노드보다 우선순위가 크므로
             * 현재 삽입 될 위치에 부모노드 값으로 교체해주고
             * 타겟 노드의 위치를 부모노드의 위치로 변경해준다.
             */
            array[idx] = parentVal;
            idx = parent;
        }
        // 최종적으로 삽입 될 위치에 타겟 노드 요소를 저장해준다.
        array[idx] = target;
    }


    // 삽입 할 객체의 Comparable을 이용한 sift-up
    @SuppressWarnings("unchecked")
    private void siftUpComparable(int idx, E target) {

        // 타겟노드가 비교 될 수 있도록 한 변수를 만든다.
        Comparable<? super E> comp = (Comparable<? super E>) target;

        // 노드 재배치 과정은 siftUpComparator와 같다.
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

    @Override
    public E poll() {

        if (array[1] == null) return null;

        return remove();
    }

    public E remove() {
        if (array[1] == null) throw new NoSuchElementException();

        E result = (E) array[1];
        E target = (E) array[size];

        array[size] = null;
        size--;
        siftDown(1, target);

        return result;
    }

    private void siftDown(int idx, E target) {
        if (comparator != null) {
            siftDownComparator(idx, target, comparator);
        } else {
            siftDownComparable(idx, target);
        }
    }

    // Comparator을 이용한 sift-down
    @SuppressWarnings("unchecked")
    private void siftDownComparator(int idx, E target, Comparator<? super E> comp) {

        array[idx] = null;    // 삭제 할 인덱스의 노드를 삭제

        int parent = idx;    // 삭제 노드부터 시작 할 부모 노드 인덱스를 가리키는 변수
        int child;    // 교환 될 자식 인덱스를 가리키는 변수

        // 왼쪽 자식 노드의 인덱스가 요소의 개수보다 작을 때 까지 반복
        while ((child = getLeftChild(parent)) <= size) {

            int right = getRightChild(parent);    // 오른쪽 자식 인덱스
            Object childVal = array[child];    // 왼쪽 자식의 값 (교환될 요소)

            /*
             * 오른쪽 자식 인덱스가 size를 넘지 않으면서
             * 왼쪽 자식이 오른쪽 자식보다 큰 경우
             * 재배치 할 노드는 작은 자식과 비교해야 하므로 child와 childVal을
             * 오른쪽 자식으로 바꾸어 준다.
             */
            if (right <= size && comp.compare((E) childVal, (E) array[right]) > 0) {
                child = right;
                childVal = array[child];
            }

            // 재배치 할 노드가 자식 노드보다 작을 경우 반복문을 종료
            if (comp.compare(target, (E) childVal) <= 0) {
                break;
            }

            /*
             * 현재 부모 인덱스에 자식 노드 값을 대체해주고
             * 부모 인덱스를 자식 인덱스로 교체
             */
            array[parent] = childVal;
            parent = child;

        }

        // 최종적으로 재배치 되는 위치에 타겟이 된 값을 넣어준다.
        array[parent] = target;

        /*
         * 용적 사이즈가 최소 용적보다는 크면서 요소의 개수가 전체 용적의 1/4 미만일 경우
         * 용적을 반으로 줄임 (단, 최소용적보단 커야 함)
         */
        if (array.length > DEFAULT_CAPACITY && size < array.length / 4) {
            resize(Math.max(DEFAULT_CAPACITY, array.length / 2));
        }

    }

    private void siftDownComparable(int idx, E target) {
        Comparable<? super E> comp = (Comparable<? super E>) target;

        array[idx] = null;
        size--;

        int parent = idx;
        int child;

        while ((child = getLeftChild(parent)) <= size) {
            int right = getRightChild(parent);

            Object childVal = array[child];

            if (right <= size && ((Comparable<? super E>) childVal).compareTo((E) array[right]) > 0) {
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

    public int size() {
        return this.size;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E peek() {
        if (array[1] == null) {    // root 요소가 null일 경우 예외 던짐
            throw new NoSuchElementException();
        }
        return (E) array[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(Object value) {
        for (int i = 1; i <= size; i++) {
            if (array[i].equals(value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }

        size = 0;
    }
}
